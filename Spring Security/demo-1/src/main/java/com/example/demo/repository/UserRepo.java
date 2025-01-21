package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SUsers;

@Repository // Marks this interface as a repository that interacts with the database
public interface UserRepo extends JpaRepository<SUsers, Integer> {

    // Custom method to find a user by their username
    SUsers findByUsername(String username); 
}
