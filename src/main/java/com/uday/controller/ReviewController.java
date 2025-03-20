package com.uday.controller;

import org.springframework.web.bind.annotation.RestController;

import com.uday.Dto.Response;
import com.uday.Dto.ReviewDto;
import com.uday.service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addReview(@RequestBody ReviewDto reviewRequestDTO) {
    	reviewService.addReview(reviewRequestDTO);
    	
        return ResponseEntity.ok("Review added successfully!");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProduct(@PathVariable Long productId) {
    	System.out.println("getting called revies");
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

//    @GetMapping("/detail/{UserId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
//        return ResponseEntity.ok(reviewService.getReviewById(id));
//    }

    @DeleteMapping("/{id}/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteReview(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteReview(id, userId);
        return ResponseEntity.ok("Review deleted successfully!");
    }
}
