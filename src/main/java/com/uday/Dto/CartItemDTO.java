package com.uday.Dto;

import com.uday.entity.Cart;
import com.uday.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
	
	private Long id;
	private Long productId;
	private String ProductName;
	private Double price;
	private String imageUrl;
	private byte[] imageData;
	private int quantity;

}
