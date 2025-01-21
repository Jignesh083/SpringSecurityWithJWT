package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.SUsers;
import com.example.demo.model.UserPrincipal;
import com.example.demo.repository.UserRepo;

@Service
public class MyUserServiceDetails implements UserDetailsService {

    @Autowired
    private UserRepo repo; // Dependency injection of UserRepo to interact with the database

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // Fetch the user from the repository by username
        SUsers user = repo.findByUsername(username);
        
        // If user is not found, throw an exception
        if (user == null) {
            System.out.println("User not found...");
            throw new UsernameNotFoundException("User Not Found...");
        }
        
        // Return a UserPrincipal object constructed with the fetched user
        return new UserPrincipal(user);
    }
    
}