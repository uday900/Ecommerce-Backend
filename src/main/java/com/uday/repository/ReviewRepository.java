package com.uday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uday.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}
