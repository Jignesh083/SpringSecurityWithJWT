package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JWTService;
import com.example.demo.service.MyUserServiceDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Makes this class a Spring-managed component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService; // Service to handle JWT token operations
    
    @Autowired
    ApplicationContext context; // Used to get beans from Spring context
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Get the "Authorization" header from the request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        // Check if the header contains a JWT token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " to get the token
            username = jwtService.extractUserName(token); // Get the username from the token
        }
        
        // If we have a username and no authentication yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get user details from the service
            UserDetails userDetails = context.getBean(MyUserServiceDetails.class).loadUserByUsername(username);
            
            // Validate the token
            if (jwtService.validateToken(token, userDetails)) {
                // Create an authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // Set details like IP address
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Save the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continue processing the request
        filterChain.doFilter(request, response);
    }
}
