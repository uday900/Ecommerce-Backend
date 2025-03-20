package com.uday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uday.Dto.Response;
import com.uday.enums.OrderStatus;
import com.uday.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	
	// fetch orders
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/fetch")
	public ResponseEntity<Response> fetchAllOrders(){
		return ResponseEntity.ok(orderService.fetchAllOrders());
	}
	
	// fetch bu user
	@PreAuthorize("hasRole('USER')")
	@GetMapping("fetch/user/{userId}")
	public ResponseEntity<Response> fetchUserOrders(
			@PathVariable Long userId){
		return ResponseEntity.ok(orderService.fetchUserOrders(userId));
	}
	
	// place order
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/place-order/{userId}")
	public ResponseEntity<Response> placeOrder(
			@PathVariable Long userId,
			@RequestParam Long productId,
			@RequestParam int quantity
			){
		System.out.println(userId + "  " + productId + " " + quantity);
		return ResponseEntity.ok(orderService.placeOrder(userId, productId, quantity));
	}
	
	// check-out
	@PreAuthorize("hasRole('USER')")
	@PostMapping("check-out/{userId}")
	public ResponseEntity<Response> checkOut(
			@PathVariable Long userId){
		return ResponseEntity.ok(orderService.checkOut(userId));
	}
	
	// chagne status of order

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("update-status/{orderId}")
	public ResponseEntity<Response> updateOrderStatus(
			@PathVariable Long orderId,
			@RequestParam OrderStatus status){
		System.out.println(status);
		return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
	}
	
	
	
}
