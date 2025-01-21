package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

@RestController // do Return in json data format and using for web api and that is provided response and controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//ALL users
	@GetMapping("/")
	public List<User> getAllUsers(){
		return this.userService.getAllUsers();
	}
	
	
//	return single user 
	@GetMapping("/{username}") //iski value niche wale method main daal ne keliye use karenge path variable annotation
	public User getUser(@PathVariable("username") String userName) {
		return this.userService.getUser(userName);
	}
	
	
	@PostMapping("/") //User class ka data aayega Json main ab jab bhi json main data aaye to @RequestBody use kare
	public User add(@RequestBody User user) { //automatically json file deserialized in Object
		return this.userService.addUser(user);
	}
}
