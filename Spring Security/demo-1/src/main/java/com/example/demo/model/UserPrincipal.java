package com.example.demo.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private SUsers user; // The user object for this class
    
    // Constructor to initialize with a user
    public UserPrincipal(SUsers user) {
        this.user = user;
    }
    
    // Returns the authorities/roles for the user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assigns a role of "SUSER" to this user (can be extended to add more roles)
        return Collections.singleton(new SimpleGrantedAuthority("SUSER"));
    }

    // Returns the password for the user
    @Override
    public String getPassword() {
        return user.getPassword(); // Returns the password stored in the SUsers object
    }

    // Returns the username for the user
    @Override
    public String getUsername() {
        return user.getUsername(); // Returns the username stored in the SUsers object
    }
}
