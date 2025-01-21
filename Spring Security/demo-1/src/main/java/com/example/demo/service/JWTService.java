package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    
    private String secretKey = "cF781A";
    
    // Constructor to generate a secret key using HmacSHA256 algorithm
    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGen.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    }
    
    // Method to generate a JWT token for a given username
    public String generatedToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims) // Add claims to the token
                .subject(username) // Set the subject (username) of the token
                .issuedAt(new Date(System.currentTimeMillis())) // Set the issued date of the token
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30)) // Set the expiration date of the token
                .and()
                .signWith(getKey()) // Sign the token with the secret key
                .compact(); // Build the token
    }

    // Method to get the secret key for signing the token
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); // Use HmacSHA key for signing
    }

    // Method to extract the username from the JWT token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // Method to extract a specific claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Method to extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Method to validate the JWT token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method to extract the expiration date from the JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
}