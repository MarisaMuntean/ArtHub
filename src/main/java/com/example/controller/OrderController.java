package com.example.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.entity.Painting;
import com.example.entity.PaymentMethod;
import com.example.entity.User;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.service.PaintingDaoService;
import com.example.service.StripeService;
import com.stripe.model.checkout.Session;

@Controller
@RequestMapping("/checkout")
public class OrderController {

	@Autowired
	private PaintingDaoService paintingService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private StripeService stripeService;

	// private static final Logger logger =
	// LoggerFactory.getLogger(OrderController.class);

	@GetMapping("/{paintingId}")
	public String showCheckoutPage(@PathVariable Long paintingId, Model model) {
		Painting painting = paintingService.findById(paintingId);

		if (painting == null)
			return "redirect:/paintings";

		Order order = new Order();
		model.addAttribute("painting", painting);
		model.addAttribute("order", order);

		return "checkout";
	}

	@PostMapping("/process")
	public String processOrder(@RequestParam("paintingId") Long paintingId, @ModelAttribute Order order,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
		User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
		Painting painting = paintingService.findById(paintingId);

		if (currentUser == null || painting == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error: User or Painting not found.");
			return "redirect:/paintings";
		}

		boolean isSold = orderRepository.existsByPaintingAndStatusNot(painting, OrderStatus.CANCELED);

		if (isSold) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Sorry this painting has been sold or another user is about to buy it!");
			return "redirect:/paintings";
		}
		order.setUser(currentUser);
		order.setPainting(painting);

		orderRepository.save(order);

		if (order.getPaymentMethod() == PaymentMethod.CARD) {
			try {
				BigDecimal price = painting.getPrice();
				Session session = stripeService.createCheckoutSession(price, painting.getTitle(), order.getId());
				return "redirect:" + session.getUrl();
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("errorMessage", "Payment failed.");
				return "redirect:/checkout/" + paintingId;
			}
		} else {
			redirectAttributes.addFlashAttribute("successMessage", "Order with cash payment placed successfully!");
			return "redirect:/paintings";
		}
	}

	@GetMapping("/success")
	public String checkoutSuccess(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order != null) {
			order.setStatus(OrderStatus.PAID);
			orderRepository.save(order);
			redirectAttributes.addFlashAttribute("successMessage",
					"Payment successful! The order with id: #" + orderId + " is confirmed.");
		}
		return "redirect:/paintings";
	}

	@GetMapping("/cancel")
	public String checkoutCancel(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("cancelMessage", "Payment was canceled for Order #" + orderId);
		return "redirect:/paintings";
	}

}
