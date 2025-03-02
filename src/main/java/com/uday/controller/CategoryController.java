package com.uday.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uday.Dto.CategoryDto;
import com.uday.Dto.Response;
import com.uday.service.CategoryService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/fetch")
	public ResponseEntity<Response> fetchCategories() {
		return ResponseEntity.ok(categoryService.fetchCategories());
	}
	
	@GetMapping("/fetch/{categoryId}")
	public ResponseEntity<Response> getCategoryById(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
	}

	// request for adding category
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<Response> addCategory(@RequestBody CategoryDto categoryDto) {
		// TODO: process POST request
		return ResponseEntity.ok(categoryService.addCategory(categoryDto));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<Response> updateCategory(@PathVariable Long categoryId,
			@RequestBody CategoryDto categoryDto) {
		// TODO: process PUT request

		return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
	}

	

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<Response> deleteCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
	}

}
