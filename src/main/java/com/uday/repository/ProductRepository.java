package com.uday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uday.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByCategoryId(Long categoryId);

//	@Query("select product from Product p where " + 
//			"lower(p.name) like lower(concat('%', :searchValue, '%')) or " + 
//			"lower(p.description) like lower(concat('%', :searchValue, '%')) or " +
//			"lower(p.category.name) like lower(concat('%', :searchValue, '%')) or" + 
//			"lower(p.brand) like lower(concat('%', :searchValue, '%'))"
//			)
//	List<Product> searchProducts(@Param("searchValue") String searchValue);
	
	@Query("SELECT p FROM Product p WHERE " + 
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " + 
		       "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
		       "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +  // Fixed missing OR
		       "LOWER(p.brand) LIKE LOWER(CONCAT('%', :searchValue, '%'))")  
		List<Product> searchProducts(@Param("searchValue") String searchValue);


}
