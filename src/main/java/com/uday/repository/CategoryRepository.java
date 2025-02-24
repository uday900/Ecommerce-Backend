package com.uday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uday.entity.Cart;
import com.uday.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String categoryName);

	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
	List<Cart> findByUserId(Long userId);

}
