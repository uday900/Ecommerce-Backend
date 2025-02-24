package com.uday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uday.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	CartItem findByProductId(Long productId);

	@Query("DELETE FROM CartItem item WHERE item.id = :cartItemId")
	void deleteItemById( @Param("cartItemId") Long cartItemId);
	
	

}
