package com.uday.Dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.uday.entity.Carousel;
import com.uday.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Response {
	
	private int status;
	private String message;
	
	private List<CategoryDto> categories;
	private CategoryDto category;
	
	private List<ProductDto> products;
	private ProductDto product;
	
	private List<CartDto> cartItems;
	private Double totalAmount;
	
	private Integer cartCount;
	
	private UserDto user;
	private List<UserDto> users;
	
	
	private CartDto cart;
	
	private List<OrderDto> orders;
	
	private OrderDto order;
	
	private List<Carousel> carouselList;
	
	private String token;
	
	
}
