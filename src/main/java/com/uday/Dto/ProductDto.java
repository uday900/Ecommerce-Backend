package com.uday.Dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductDto {

	private Long id;
	private String name;
	private Double price;
	private String description;
	private List<String> colors;
	private List<String> sizes;
	private String brand;
	private float rating;
	private String categoryName;
	
	private String imageUrl;
	private String imageName;
	private byte[] imageData;
	
}
