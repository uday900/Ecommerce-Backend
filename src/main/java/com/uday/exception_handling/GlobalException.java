package com.uday.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.uday.Dto.Response;

@RestControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<Response> notFoundException(NotFoundException msg){
		Response res = new Response();
		res.setStatus(401);
		res.setMessage(msg.toString());
		return ResponseEntity.ok(res);
//		<>(msg.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<Response> handleGlobalExceptions(Exception msg){
		Response res = new Response();
		res.setStatus(401);
		res.setMessage(msg.toString());
		return ResponseEntity.ok(res);
		
//		return new ResponseEntity<>(msg.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
