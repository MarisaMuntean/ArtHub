package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ArtHubSpringBootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtHubSpringBootAppApplication.class, args);
	}
	@Bean
	public CommandLineRunner generatePassword() {
		return args -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String rawPassword = "admin";
			String encodedPassword = encoder.encode(rawPassword);
            
			System.out.println("=========================================");
			System.out.println("PAROLA CRIPTATĂ PENTRU 'admin' ESTE:");
			System.out.println(encodedPassword);
			System.out.println("=========================================");
		};
	}
	

}
