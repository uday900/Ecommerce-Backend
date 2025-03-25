package com.uday.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.uday.Dto.Response;

@RestControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<Response> notFoundException(NotFoundException msg){
		Response res = new Response();
		res.setStatus(404);
		res.setMessage(msg.getMessage());
		return ResponseEntity.ok(res);
	}
	
	// restrict the user to access the resource
	@ExceptionHandler(AuthorizationDeniedException.class)
	ResponseEntity<String> authorizationDeniedException(AuthorizationDeniedException msg) {
		return new ResponseEntity<>(msg.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	// handle invalid session or token
	@ExceptionHandler(InvalidSessionOrToken.class)
	ResponseEntity<String> invalidSessionOrToken(InvalidSessionOrToken msg) {
		return new ResponseEntity<>(msg.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	// Global exception handler
	@ExceptionHandler(Exception.class)
	ResponseEntity<Response> handleGlobalExceptions(Exception msg){
		Response res = new Response();
		res.setStatus(404);
		res.setMessage(msg.getMessage());
		return ResponseEntity.ok(res);
		
//		return new ResponseEntity<>(msg.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
