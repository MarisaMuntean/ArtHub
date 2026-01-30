package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/register","/register/save","/login","/styles.css").permitAll()
				.requestMatchers("/paintings","/paintings/find","/paintings/filter-advanced","/uploads/**","/checkout/**").authenticated()
				.requestMatchers("/paintings/add","/paintings/delete/**","/paintings/update").hasRole("EDITOR")
				.anyRequest().authenticated()
				)
		.formLogin(form -> form
			    .loginPage("/login") 
			    .defaultSuccessUrl("/paintings", true)
			    .permitAll()
			)
		.logout(logout -> logout.logoutSuccessUrl("/login?logout")
				.permitAll());
	
		return http.build();
	}
}
