package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.SUsers;
import com.example.demo.service.UserService;

@RestController // Marks this class as a REST controller
public class UserController {

    @Autowired
    private UserService service; // Injects the UserService to handle business logic
    
    // Endpoint to register a new user
    @PostMapping("/register")
    public SUsers register(@RequestBody SUsers user) {
        return service.register(user); // Calls the service to register the user and returns the user details
    }
    
    // Endpoint to log in a user by verifying their credentials
    @PostMapping("/login")
    public String login(@RequestBody SUsers user) {
        return service.verify(user); // Calls the service to verify the user's credentials and returns a response
    }
}
