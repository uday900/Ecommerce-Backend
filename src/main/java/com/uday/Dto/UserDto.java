package com.uday.Dto;

import com.uday.entity.Address;
import com.uday.enums.UserRole;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

	private Long id;
	private String name;
	private String email;
	private String password;
	private UserRole role;
	
	private Address address;
	private Long phoneNumber;
//	private String resetPasswordToken;
//	private String resetPasswordExpires;
}
