package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Controller
public class AuthentificationController {

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
        model.addAttribute("user", new User());
        return "register";
    }
	
	@PostMapping("/register/save")
	public String registerUser(User user, Model model)
	{
		if(userRepository.findByUsername(user.getUsername()).isPresent())
		{
			model.addAttribute("error","Username is taken.");
			return "register";
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("USER");
		userRepository.save(user);
		return "redirect:/login?registered";
	}
	
}
