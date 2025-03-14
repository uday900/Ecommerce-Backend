package com.uday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uday.Dto.CartDto;
import com.uday.Dto.CartItemDTO;
import com.uday.Dto.Response;
import com.uday.Dto.UserDto;
import com.uday.service.TokenBlacklistService;
import com.uday.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "${frontend.url}")
public class UserController {
	
	private final UserService userService;
	
	@Autowired
    private TokenBlacklistService tokenBlacklistService;
	
	
	
	// get all users
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/fetch")
	public ResponseEntity<Response> getAllUsers(){
		return ResponseEntity.ok(
				userService.getAllUsers()
				);
	}
	
	
	// get user by id
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/fetch/{userId}")
	public ResponseEntity<Response> getUserById(@PathVariable Long userId) {
		System.out.println(userId);
		return ResponseEntity.ok(userService.getUserById(userId));
	}
	
	
	
	
	// request user cart
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/cart/fetch/{userId}")
	public ResponseEntity<Response> getUserCart(
			@PathVariable Long userId) {
		return ResponseEntity.ok(userService.getUserCart(userId));
	}
	
	// add to cartd
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/cart/add/{userId}")
	public ResponseEntity<Response> addItemtoCart(
			@PathVariable Long userId,
			@RequestParam Long productId
//			@RequestBody CartItemDTO cartItemDto) 
			)
	{
		return ResponseEntity.ok(userService.addItemtoCart(userId, productId));
	}
	
	// remove from cart
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/cart/remove/{userId}/{cartItemId}")
	public ResponseEntity<Response> removeItemFromCart(
			@PathVariable Long userId,
			@PathVariable Long cartItemId) {
		return ResponseEntity.ok(userService.removeCartItem(userId, cartItemId));
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/cart/count/{userId}")
	public ResponseEntity<Response> getCartCount(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.getCartCount(userId));
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/cart/increment/{userId}")
	public ResponseEntity<Response> incrementQuantity(
			@PathVariable Long userId,
			@RequestParam Long cartItemId) {
		//TODO: process POST request
		
		return ResponseEntity.ok(userService.incrementQuantity(userId, cartItemId));
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/cart/decrement/{userId}")
	public ResponseEntity<Response> decrementQuantity(
			@PathVariable Long userId,
			@RequestParam Long cartItemId) {
		//TODO: process POST request
		
		return ResponseEntity.ok(userService.decrementQuantity(userId, cartItemId));
	}
	
	
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
		
		if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
		} else {
			return ResponseEntity.badRequest().body("Invalid token");
		}
		
		tokenBlacklistService.addToBlacklist(token);
		SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully.");
		
		
	}	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
