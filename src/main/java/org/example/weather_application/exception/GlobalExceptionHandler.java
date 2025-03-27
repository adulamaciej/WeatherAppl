package org.example.weather_application.exception;


import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public String handleCityNotFoundException(Model model) {
        model.addAttribute("errorMessage", "City not found. Please check the spelling and try again.");
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("errorMessage", "Validation error: " + ex.getMessage());
        return "error";

    }
    @ExceptionHandler(Exception.class)
    public String handleException(Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error";
    }

}