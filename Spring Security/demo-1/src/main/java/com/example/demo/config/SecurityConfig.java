package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this class as a configuration class for Spring
@EnableWebSecurity // Enables Spring Security's web security support
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // Custom service to load user data

    @Autowired
    private JwtFilter jwtFilter; // Custom filter for JWT authentication

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(customizer -> customizer.disable()) // Disables CSRF protection (not needed for stateless APIs)
            .authorizeHttpRequests(request -> request
                .requestMatchers("register", "login").permitAll() // Allow access to 'register' and 'login' without authentication
                .anyRequest().authenticated()) // Requires authentication for all other requests
            .httpBasic(Customizer.withDefaults()) // Enables basic HTTP authentication for REST API
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions (stateless authentication)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter before the standard authentication filter
            .build(); // Builds the SecurityFilterChain
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Create users in memory with roles and passwords (plain text passwords for now)
        UserDetails user1 = User.withUsername("user1")
            .password("{noop}RS@321") // {noop} means no password encryption (plain text)
            .roles("USER") // Assigns 'USER' role to this user
            .build();

        UserDetails user2 = User.withUsername("user3")
            .password("{noop}RS@3211") // Plain text password
            .roles("ADMIN") // Assigns 'ADMIN' role to this user
            .build();

        return new InMemoryUserDetailsManager(user1, user2); // Store users in memory
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // Use DAO-based authentication
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // Use BCrypt encryption for passwords
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Uncomment to use plain text passwords
        provider.setUserDetailsService(userDetailsService); // Use the custom user details service
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Gets the authentication manager to manage user authentication
    }
}
