package com.uday.entity;

import jakarta.persistence.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;


@Entity
@Table(name = "cart")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private Double totalAmount;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<CartItem> cartItems;
	
}


//@Entity
//@Table(name = "cart", uniqueConstraints = {
//		@UniqueConstraint(columnNames = { "user_id", "product_id"})
//})
//@Builder
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Cart {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	@ManyToOne
//	@JoinColumn(name = "user_id")
//	private User user;
//	
////	@OneToOne
//	@ManyToOne
//	@JoinColumn(name = "product_id")
//	private Product product;
//	
//	private Integer quantity;	
//	
//	
//	
//}
