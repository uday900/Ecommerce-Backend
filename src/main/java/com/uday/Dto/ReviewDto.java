package com.uday.Dto;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

	private Long id;
	
    private Long productId;

    private Long userId;
    
    private String userName;

    private int rating;

    
    private String comment;
    
    private LocalDateTime createdAt;
}

