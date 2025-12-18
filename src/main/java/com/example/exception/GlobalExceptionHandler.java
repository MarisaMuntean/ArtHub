package com.example.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaintingNotFoundException.class)
    public String handlePaintingNotFound(PaintingNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "paintings";
    }
    
}
