	package com.uday.service;
	
	import java.util.List;
	import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
	
	import com.uday.Dto.CategoryDto;
	import com.uday.Dto.Response;
	import com.uday.entity.Category;
import com.uday.entity.Product;
import com.uday.mapper.EntityAndDtoMapper;
	import com.uday.repository.CategoryRepository;
import com.uday.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
	
	@Service
	@RequiredArgsConstructor
	public class CategoryService {
	
		private final CategoryRepository categoryRepository;
		private final EntityAndDtoMapper entityAndDto;
		
		@Autowired
		private ProductRepository productRepository;
		
		//update category
		
		// delete category	

		public Response updateCategory(Long categoryId, CategoryDto categoryDto) {
			Category category = categoryRepository.findById(categoryId).orElse(null);
			
			if (category == null) {
				return Response.builder().status(404).message("Category not found").build();
			}
			
			if ( categoryDto.getName() == null || categoryDto.getName().trim() == "" ) {
				return Response.builder()
						.status(400)
						.message("Category can't be empty")
						.build();
			}
			category.setName(categoryDto.getName());
			
			categoryRepository.save(category);
			
			return Response.builder().status(200).message("category updated success").build();
					
		}
		
		
		
		public Response deleteCategory(Long categoryId) {
	
			Category category = categoryRepository.findById(categoryId).orElse(null);
			
//			Response res = new Response();
			if (category == null) {
				return Response.builder().status(404).message("Category not found").build();
			}
	
//			List<Product> products = category.getProducts();
//			
//			if (!products.isEmpty()) {
//				System.out.println("this category has products to be delete");
//				productRepository.deleteByCategoryId(category.getId());
//				
//			}
			categoryRepository.delete(category);
	
			return Response.builder()
					.status(200).message("Category deleted successfully").build();
		}
		// get by id
		public Response getCategoryById(Long categoryId) {
	        
	        Category category = categoryRepository.findById(categoryId).orElse(null);
	        
	        if(category == null) {
	            return Response.builder()
	                    .status(404)
	                    .message("Category not found")
	                    .build();
	        }
	        
	        CategoryDto categoryDto = entityAndDto.mapToCategoryDto(category);
	        
	        return Response.builder()
	                .status(200)
	                .message("Category found")
	                .category(categoryDto)
	                .build();
		}
		// add category
		public Response addCategory(CategoryDto categoryDto) {
	
			System.out.println(categoryDto.getName());
			if ( categoryDto.getName() == null || categoryDto.getName().trim() == "" ) {
				return Response.builder()
						.status(400)
						.message("Category can't be empty")
						.build();
			}
			Category newCategory = new Category();
			newCategory.setName(categoryDto.getName());
			
			
			categoryRepository.save(newCategory);
			
			return Response.builder()
					.status(200)
					.message("Category added successfully")
					.build();
		}
		
		public Response fetchCategories() {
		
			List<CategoryDto> categories = categoryRepository.findAll()
					.stream().map(entityAndDto::mapToCategoryDto)
					.collect(Collectors.toList());
			
			
			return Response.builder()
					.status(200)
//					.message("Success")
					.categories(categories)
					.build();
		}
		
	}
