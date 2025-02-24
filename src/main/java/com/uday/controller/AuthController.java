package com.uday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uday.Dto.LoginRequest;
import com.uday.Dto.Response;
import com.uday.Dto.UserDto;
import com.uday.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	// register user
	@PostMapping("/register")
	public ResponseEntity<Response> registerUser(@RequestBody UserDto userDto) {
		return ResponseEntity.ok(userService.registerUser(userDto));
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<Response> authenticateUser(@RequestBody LoginRequest loginRequest){
		System.out.println("calling");
		return ResponseEntity.ok(userService.authenticateUser(loginRequest));
	}
	

	@PostMapping("/forgot-password")
	public ResponseEntity<Response> forgotPassword( 
			@RequestParam String email){
		return ResponseEntity.ok(userService.forgotPassword(email) );
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<Response> resetPassword( 
			@RequestParam String newPassword,
			@RequestParam String token,
			@RequestParam String email){
		return ResponseEntity.ok(
				userService.resetPassword( email, newPassword, token)
				);
	}


}
