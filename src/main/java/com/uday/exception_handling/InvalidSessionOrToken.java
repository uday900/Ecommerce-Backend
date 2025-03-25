package com.uday.exception_handling;

public class InvalidSessionOrToken extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSessionOrToken(String message) {
		super(message);
	}
}
