package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController // Marks this class as a REST controller
public class StudentController {
    
    // In-memory list of students
    private List<Student> students = new ArrayList<>(List.of(
            new Student(1, "Jignesh", 89),
            new Student(2, "Rahul", 70)
    ));
    
    // Endpoint to get the list of all students
    @GetMapping("/students")
    public List<Student> getStudents() {
        return students; // Returns the list of students
    }
    
    // Endpoint to get the CSRF token (used for security in POST requests)
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest req) {
        return (CsrfToken) req.getAttribute("_csrf"); // Retrieves the CSRF token from the request
    }
    
    // Endpoint to add a new student to the list
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        students.add(student); // Adds the new student to the list
        return student; // Returns the added student as a response
    }
}
