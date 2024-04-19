package com.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Generic exception handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        logger.error("Error occurred: ", ex);
        model.addAttribute("errMsg", "An error occurred: " + ex.getMessage());
        return "errorPage";  // Assuming you have a generic error page template
    }

    // Add more handlers here for specific exceptions as needed, for example:
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNullPointerException(NullPointerException ex, Model model) {
        model.addAttribute("errMsg", "Null values found: " + ex.getMessage());
        return "errorPage";  // Redirect to a specific error page for null pointer exceptions
    }
}

