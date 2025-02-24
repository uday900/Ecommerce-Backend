package com.uday.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uday.Dto.Response;
import com.uday.entity.Carousel;
import com.uday.repository.CarouselRepository;

import jakarta.mail.Multipart;

@Service
public class CarouselService {
	
	@Autowired
	private CarouselRepository carouselRepository;
	
	public Response fetchAllCarouselImages() {
		List<Carousel> list = carouselRepository.findAll();
		
		return Response.builder()
				.status(200)
				.carouselList(list)
				
				.build();
	}
	
	public Response addNewCarouselImage(MultipartFile image) throws IOException {
		
		Carousel newImage = new Carousel();
		newImage.setImageName(image.getOriginalFilename());
		newImage.setImageData(image.getBytes());
		
		carouselRepository.save(newImage);
		return Response.builder()
				.message("added")
				.status(200)
				
				
				.build();
		
	}
	public Response removeCarouselImage(Long id) {
		Carousel existing = carouselRepository.findById(id).orElse(null);
		if (existing != null ) {
			carouselRepository.deleteById(id);
			return Response.builder()
					.message("deleted")
					.status(200)
					
					
					.build();
		}
		return Response.builder()
				.message("nor found")
				.status(400)
				
				
				.build();
	}

}
