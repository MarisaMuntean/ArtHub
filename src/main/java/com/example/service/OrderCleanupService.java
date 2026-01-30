package com.example.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.entity.PaymentMethod;
import com.example.repository.OrderRepository;

@Service
public class OrderCleanupService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Scheduled(fixedRate = 60000)
	@Transactional
	public void cancelUnpaidOrders()
	{
		LocalDateTime cutoffTime  = LocalDateTime.now().minusMinutes(10);
		
		List<Order> expiredOrders = orderRepository.findByStatusAndPaymentMethodAndOrderDateBefore(OrderStatus.PENDING, PaymentMethod.CARD, cutoffTime);
		
		for(Order order : expiredOrders)
		{
			System.out.println("Cancelled unpaid order with id #" + order.getId());
			
			order.setStatus(OrderStatus.CANCELED);
			orderRepository.save(order);
		}
		
	}
}
