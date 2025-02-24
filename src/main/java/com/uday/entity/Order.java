package com.uday.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.uday.enums.OrderStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    private int quantity;
     
    private Double totalAmount;
    
    private LocalDateTime orderDate;

//    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, SHIPPED, DELIVERED, CANCELED
}
