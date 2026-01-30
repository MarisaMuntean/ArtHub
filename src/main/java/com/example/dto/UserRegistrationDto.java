package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Username is compulsor.y")
    @Size(min = 4, max = 20, message = "Username must have between 4 and 20 characters.")
    private String username;

    @NotBlank(message = "Email address is compulsory.")
    @Email(message = "Enter a valid email address.")
    private String email;

    @NotBlank(message = "Password is compulsory.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", 
             message = "Password must contain at least a capital letter, a lowercase letter and a number.")
    private String password;

    @NotBlank(message = "Confirm the password.")
    private String confirmPassword;
}