package com.uday.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uday.Dto.ProductDto;
import com.uday.Dto.Response;
import com.uday.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class ProductController {

	private final ProductService productService;
	
	// delete product
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<Response> deleteProduct(@PathVariable Long productId) throws GeneralSecurityException, IOException {
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}
	
	// update product
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{productId}")
	public ResponseEntity<Response> updateProduct(
			@ModelAttribute ProductDto productDto,
			@RequestParam(required = false, name = "image") MultipartFile image,
			@PathVariable Long productId
			) throws IOException, GeneralSecurityException 
	{
		return ResponseEntity.ok(productService.updateProduct(productId, productDto,  image));
	}
	
	// fetch product by id
	@GetMapping("/fetch/{id}")
	public ResponseEntity<Response> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}
	
	// fetch product by search keyword
	@GetMapping("/fetch/search")
	public ResponseEntity<Response> getProductsBySearchKeyword(
			@RequestParam String searchValue) {
		return ResponseEntity.ok(productService.getProductsBySearchKeyword(searchValue));
	}
	
	// fetch product by category
	@GetMapping("/fetch/category")
	public ResponseEntity<Response> getProductsByCategory(
			@RequestParam String categoryName) {
		System.out.println(categoryName);
		return ResponseEntity.ok(productService.getProductsByCategory(categoryName));
	}
	
	// fetch all products
	@GetMapping("/fetch")
	public ResponseEntity<Response> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	// test image upload
	@PostMapping("/test")
	public ResponseEntity<Response> testImageUpload(@RequestParam("image") MultipartFile image)
			throws IOException, GeneralSecurityException {
		return ResponseEntity.ok(productService.testImageUpload(image));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<Response> createProduct(
			@ModelAttribute ProductDto productDto,
			@RequestParam("image") MultipartFile image
			
			) throws IOException, GeneralSecurityException {
//		Response res = ;
		
		return ResponseEntity.ok(productService.createProduct(productDto, image));
	}
}
