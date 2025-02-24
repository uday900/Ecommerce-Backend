package com.uday.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uday.Dto.Response;
import com.uday.service.CarouselService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/carousels")
public class CarouselContoller {

	@Autowired
	private CarouselService carouselService;
	
	
	@GetMapping("/fetch")
	public Response fetchAllCarouselImages() {
		return carouselService.fetchAllCarouselImages();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public Response addNewCarouselImage(@RequestParam MultipartFile image) throws IOException {
		//TODO: process POST request
		
		return carouselService.addNewCarouselImage(image);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public Response removeCarouselImage(@PathVariable Long id) {
		return carouselService.removeCarouselImage(id);
	}
	
	
}
