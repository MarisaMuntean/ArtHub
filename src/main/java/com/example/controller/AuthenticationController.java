package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.UserRegistrationDto;
import com.example.entity.User;
import com.example.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("user", new UserRegistrationDto());
		return "register";
	}

	@PostMapping("/register/save")
	public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto userDto, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "register";
		}

		if (userDto.getPassword() != null && userDto.getConfirmPassword() != null) {
			if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
				result.rejectValue("confirmPassword", "error.user", "Passwords do not match.");
			}
		}

		if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			result.rejectValue("username", "error.user", "Username is already taken.");
			return "register";
		}

		if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
			result.rejectValue("email", "error.user", "This email is already registered.");
		}

		if (result.hasErrors()) {
			return "register";
		}

		User newUser = new User();
		newUser.setUsername(userDto.getUsername());
		newUser.setEmail(userDto.getEmail()); // Asigură-te că ai adăugat câmpul email în User.java
		newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		newUser.setRole("USER");

		userRepository.save(newUser);

		redirectAttributes.addFlashAttribute("successMessage", "Account created! Please log in.");
		return "redirect:/login";
	}

}
