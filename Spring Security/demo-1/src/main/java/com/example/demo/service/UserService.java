package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.SUsers;
import com.example.demo.repository.UserRepo;

@Service
public class UserService {
    
    @Autowired
    UserRepo userRepo; // Dependency injection of UserRepo to interact with the database
    
    @Autowired
    JWTService jwtService; // Dependency injection of JWTService to handle JWT operations
    
    @Autowired
    AuthenticationManager authManager; // Dependency injection of AuthenticationManager to handle authentication
    
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5); // BCryptPasswordEncoder with strength 5 for password encoding
    
    // Method to register a new user
    public SUsers register(SUsers user) {
        // Encode the user's password using BCrypt and save the user to the repository
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
    
    // Method to verify the user's credentials and generate a JWT token
    public String verify(SUsers user) {
        // Authenticate the user using the provided username and password
        Authentication authentication = 
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        // If authentication is successful, generate and return a JWT token
        if (authentication.isAuthenticated()) {
            return jwtService.generatedToken(user.getUsername());
        }
        
        // If authentication fails, return "fail"
        return "fail";
    }
}