package com.uday.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.uday.enums.UserRole;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private UserRole role;
	
	@Embedded
	private Address address;
	
	private Long phoneNumber;
	
	private String resetPasswordToken;
	
	private LocalDateTime resetPasswordExpires;
	
//	@JsonIgnore
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	@JsonManagedReference
//	private List<Order> orders;
	
}
