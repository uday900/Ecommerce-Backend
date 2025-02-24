package com.uday.Dto;

import java.util.List;

import com.uday.entity.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {

	private Long id;

	private Long userId;

	private Double totalAmount;
	
//	private CartItemDTO item;
	
	private List<CartItemDTO> cartItems;
}
