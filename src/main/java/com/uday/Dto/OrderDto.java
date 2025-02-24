package com.uday.Dto;

import java.time.LocalDateTime;

import com.uday.enums.OrderStatus;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDto {

	    private Long id;
	    
	    private Long userId;
	    
	    private Long productId;
	    
	    private String productName;
	    
	    private int quantity;
	     
	    private Double totalAmount;
	    
	    private LocalDateTime orderDate;

	    private OrderStatus status;
	
}
