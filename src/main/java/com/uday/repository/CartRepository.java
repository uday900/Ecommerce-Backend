package com.uday.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uday.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByUserId(Long userId);

//	@Query( "SELECT c from cart c WHERE c.userId = :userId AND c.productId = :productId")

//	
//	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId")
//	Optional<Cart> checkProductExistsWithUser(@Param("userId") Long userID,@Param("productId") Long productId);

//	
//	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId")
//	Optional<Cart> checkProductExistsWithUser(@Param("userId") Long userId, @Param("productId") Long productId);
//
////	List<Cart> findByUser(User user);
//	
//	List<Cart> findByUserId(Long userId);

//	Cart findByUser(User user);

}
