package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	UserRepository repository;
	
	@GetMapping("/users")
	public String getUsers(Model model) {
	String s="List of current users: ";
	model.addAttribute("str",s);
	model.addAttribute("listUsers",repository.findAll());
	return "users";
	}
}
