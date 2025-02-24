package com.uday.entity;

import jakarta.persistence.Embeddable;

@Embeddable
@lombok.Data
public class Address {
	private String street;
	private String city;
	private String state;
	private String country;
	private String zipCode;
}
