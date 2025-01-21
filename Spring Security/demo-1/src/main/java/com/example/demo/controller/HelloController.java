package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController // This tells Spring that this class will handle web requests and return data (like text or JSON)
public class HelloController {
    
    @GetMapping("/") // This means when someone visits the root URL ("/"), this method will run
    public String greet(HttpServletRequest req) {
        // Returns a welcome message and the session ID of the user
        return "Welcome to Jignesh " + req.getSession().getId();
    }
}
