package com.uday.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uday.Dto.OrderDto;
import com.uday.Dto.Response;
import com.uday.entity.Cart;
import com.uday.entity.CartItem;
import com.uday.entity.Order;
import com.uday.entity.Product;
import com.uday.entity.User;
import com.uday.enums.OrderStatus;
import com.uday.mapper.EntityAndDtoMapper;
import com.uday.repository.CartRepository;
import com.uday.repository.OrderRepository;
import com.uday.repository.ProductRepository;
import com.uday.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private EntityAndDtoMapper mapper;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	// check-out
	public Response checkOut( Long userId ) {
		User user = userRepository.findById(userId).orElse(null);
		if (user == null ) {
			return Response.builder()
					.message("user not found")
					.status(400)
					.build();
		}
		
		Cart userCart = cartRepository.findByUserId(userId);
		List<CartItem> cartItems = userCart.getCartItems();
		
		for ( CartItem item : cartItems ) {
			
			Product product = productRepository.findById(item.getProduct().getId())
					.orElse(null);
			Order newOrder = Order.builder()
					.user(user)
					.product(product)
					.quantity(item.getQuantity())
					.totalAmount( product.getPrice() * item.getQuantity())
					.orderDate(LocalDateTime.now())
					.status(OrderStatus.PENDING)
					
					.build();
			
			orderRepository.save(newOrder);
			System.out.println("order placed for product " + product.getId());
		}
		return Response.builder()
				.status(200)
				.message("check-out created")
				.build(); 
	}
	
	// change status of order
	public Response updateOrderStatus(Long orderId, OrderStatus status) {
		// check if the order exists
		Order order = orderRepository.findById(orderId).orElse(null);
		if ( order != null ) {
			order.setStatus(status);
			System.out.println("status updated with "+ status + 
					"orderId " + orderId);
			
			orderRepository.save(order);
			 
			return Response.builder()
					.message("status updated")
					.status(200)
					.build();
		}
		return Response.builder()
				.message("order not found")
				.status(400)
				.build();
	}
	
	// fetch user orders
	public Response fetchUserOrders( Long userId ) {
		User user = userRepository.findById(userId).orElse(null);
		if ( user == null ) {
			return Response.builder().message("user not found").build();
		}
		List<OrderDto> orders = orderRepository.findByUser(user)
				.stream()
				.map(mapper::mapToOrderDto)
				.collect(Collectors.toList());
		
		return Response.builder()
				.status(200)
				.orders(orders)
				.build();
	}
	
	
	// fetch all orders
	public Response fetchAllOrders() {
		List<OrderDto> orders = orderRepository.findAll()
				.stream()
				.map(mapper::mapToOrderDto)
				.collect(Collectors.toList());
		
		return Response.builder()
				.status(200)
				.orders(orders)
				.build();
	}
	
	// place an order to userId
	// assume userId comes as param and other as queryParams
	public Response placeOrder( Long userId, Long productId , int quantity) {
		User user = userRepository.findById(userId).orElse(null);
		Product product = productRepository.findById(productId).orElse(null);
		if ( user == null || product == null ) {
			return Response.builder()
					.status(404)
					.message("user/product not found")
					.build();
		}
		
		Order order = Order.builder()
				.user(user)
				.product(product)
				.quantity(quantity)
				.totalAmount( product.getPrice() * quantity)
				.orderDate(LocalDateTime.now())
				.status(OrderStatus.PENDING)
				.build();
		
		orderRepository.save(order);
		return Response.builder()
				.status(200)
				.message("order created")
				.build();
	}
}
