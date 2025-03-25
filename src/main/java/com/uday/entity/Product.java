package com.uday.entity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private Double price;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	private String colors;
	
	private String sizes;
	
	private String brand;
	
	private float rating;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
	
	private String imageName;
	
	 @Lob
//	    @Column(columnDefinition = "BYTEA") // for postgresql
	    @Basic(fetch = FetchType.EAGER) // for postgresql
	    private byte[] imageData;
	
	private String imageUrl;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();
    
    

}
