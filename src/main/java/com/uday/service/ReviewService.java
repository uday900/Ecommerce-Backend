package com.uday.service;

import org.springframework.stereotype.Service;

import com.uday.Dto.ReviewDto;
import com.uday.entity.*;
import com.uday.entity.Review;
import com.uday.exception_handling.NotFoundException;
import com.uday.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

	
    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
   

	public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
      
        }


    public void addReview(ReviewDto reviewDto) {
    	
    	// check if the product exists or not
    	Product product = productRepository.findById(reviewDto.getProductId()).orElseThrow(
    			() -> new NotFoundException("Product not found with id: " + reviewDto.getProductId()));
    	
    	// now check if the user exists or not
		User user = userRepository.findById(reviewDto.getUserId())
				.orElseThrow(() -> new NotFoundException("User not found with id: " + reviewDto.getUserId()));

		//  check rating is between 1 to 5
		if (reviewDto.getRating() < 1 || reviewDto.getRating() > 5) {
			throw new IllegalArgumentException("Rating should be between 1 to 5");
		}
		
		if (reviewDto.getComment().length() > 500 || reviewDto.getComment().length() < 1) {
			throw new IllegalArgumentException("Comment should not exceed 500 characters/ cannot be empty");
		}
		// now save the review
		Review review = new Review();
		review.setProduct(product);
		review.setUser(user);
		
		review.setRating(reviewDto.getRating());
		review.setComment(reviewDto.getComment());
		reviewRepository.save(review);
		
		
		// update the average rating of the product
		
		List<Review> reviews = reviewRepository.findByProductId(product.getId());
		double avgRating = (product.getRating() * reviews.size() + reviewDto.getRating() ) / (reviews.size() + 1.0);
		
		avgRating = Math.round(avgRating * 10.0) / 10.0;
		product.setRating((float) avgRating);
		
		productRepository.save(product);
		
		
    	
    }

    public List<ReviewDto> getReviewsByProduct(Long productId) {
    	// check if the product exists or not
    	productRepository.findById(productId).orElseThrow(
    			() -> new NotFoundException("Product not found with id: " + productId));
    	
    	// map to dto
    	List<Review> reviews = reviewRepository.findByProductId(productId);
    	
    	List<ReviewDto> reviewDtos = reviews.stream().map((review) -> {
    		ReviewDto reviewDto = new ReviewDto();
    		reviewDto.setId(review.getId());
    		reviewDto.setProductId(productId);
    		reviewDto.setUserId(review.getUser().getId());
    		reviewDto.setUserName(review.getUser().getName());
    		reviewDto.setRating(review.getRating());
    		reviewDto.setComment(review.getComment());
    		reviewDto.setCreatedAt(review.getCreatedAt());
    		return reviewDto;
    	}).collect(Collectors.toList());
    
   
        return reviewDtos;
    }

    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("Review not found with id: " + id));
        
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setProductId(review.getProduct().getId());
		return reviewDto;
    }

    public void deleteReview(Long id, Long userId) {
    	userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    	
    	Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("Review not found with id: " + id));
    	
    	List<Review> reviews = reviewRepository.findByProductId( review.getProduct().getId());
    	
		Product product = productRepository.findById(review.getProduct().getId())
				.orElseThrow(() -> new NotFoundException("Product not found with id: " + review.getProduct().getId()));
		
		if (reviews.size() == 1) {
			product.setRating(0.0f);
		} else {
			double avgRating = (( product.getRating() * reviews.size()) - review.getRating()) / (reviews.size() - 1);
			float roundedRating = Math.round(avgRating * 10) / 10.0f;
			
	    	product.setRating(roundedRating);
		}
    	
    	
    	productRepository.save(product);
    	
    	
        reviewRepository.deleteById(id);
    }
}
