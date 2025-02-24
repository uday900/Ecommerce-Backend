package com.uday.mapper;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.uday.Dto.*;
import com.uday.entity.Cart;
import com.uday.entity.Category;
import com.uday.entity.Order;
import com.uday.entity.Product;
import com.uday.entity.User;
import com.uday.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
public class EntityAndDtoMapper {
	
	public OrderDto mapToOrderDto( Order order ) {
		
		return OrderDto.builder()
				.id(order.getId())
				.userId(order.getUser().getId())
				.productId(order.getProduct().getId())
				.productName(order.getProduct().getName())
				.quantity(order.getQuantity())
				.totalAmount(order.getTotalAmount())
				.orderDate(order.getOrderDate())
				.status(order.getStatus())
				
				.build();
	}
	
	public UserDto mapToUserDto(User user) {
		return UserDto.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.address(user.getAddress())
		
				.build();
	}
	
	
	// map to cart
//	public CartDto mapToCartDto(Cart cart) {
//		return CartDto.builder().id(cart.getId()).quantity(cart.getQuantity())
//				.product(mapToProductDto(cart.getProduct())).build();
//	}	
	
	
	// map to cartDto
//	public CartDto mapToCartDto(Cart cart) {
//		return CartDto.builder()
//				.id(cart.getId())
//				.userId(cart.getUser().getId())
//				.quantity(cart.getQuantity())
//				.product(mapToProductDto(cart.getProduct()))
//				.build();
//	}
	
	// map to Cart
	
	public Product mapToProduct(ProductDto productDto) {
		Product product = new Product();
		
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		product.setDescription(productDto.getDescription());
		
		product.setColors(
				String.join(",", productDto.getColors()));
		 product.setSizes(String.join(",", productDto.getSizes()));
		 product.setBrand(productDto.getBrand());
		 product.setRating(productDto.getRating());
		 
		 // set the category
//		 Category category = categoryRepository.findByName(productDto.getCategoryName());
//		 
//		 product.setCategory(category);
//		 
		 // set the image url
		 product.setImageUrl(productDto.getImageUrl());
		 

		return product;
	}
	
	public ProductDto mapToProductDto(Product product) {
		
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setPrice(product.getPrice());
		productDto.setDescription(product.getDescription());
		
		productDto.setColors( Arrays.asList(product.getColors().split(",")));
		productDto.setSizes(Arrays.asList(product.getSizes().split(",")));
		
		productDto.setBrand(product.getBrand());
		productDto.setRating(product.getRating());
		productDto.setCategoryName(product.getCategory().getName());
		
		// set the image url
		productDto.setImageUrl(product.getImageUrl());
		productDto.setImageName(product.getImageName());
		productDto.setImageData(product.getImageData());
		
		return productDto;
	
	}
	public CategoryDto mapToCategoryDto(Category category) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName());
		return categoryDto;
	}

	
}
