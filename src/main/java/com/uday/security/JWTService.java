package com.uday.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	private String secretKey = "";
	
	public JWTService() {
		
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error while generating key");
		}
	}
	
	
//	 public JWTService() {
//
//	        try {
//	            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//	            SecretKey sk = keyGen.generateKey();
//	            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
//	        } catch (NoSuchAlgorithmException e) {
//	            throw new RuntimeException(e);
//	        }
//	    }

	public String generateToken(String username) {
		
		
		Map<String , Object> claims = new HashMap<>();
		
		return Jwts.builder()
				
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))) // setting 1 day as expiration
				.and()
				.signWith(getKey())
				
				.compact();
		
	}

	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}


	public String extractUserName(String token) {
		// TODO Auto-generated method stub
		
		return extractClaim( token, Claims::getSubject);
	}

	private <T> T extractClaim( String token, Function<Claims, T> claimResover) {
		final Claims claims = extractAllClaims(token);
		return claimResover.apply(claims);
		
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
				
				
				
	}
	


	


	public boolean isValidToken(UserDetails userDetails, String token) {
		// TODO Auto-generated method stub
		final String username = extractUserName(token);
		
		return username.equals(userDetails.getUsername()) && !isTokenExpire(token);
		
	}
	
	public boolean isTokenExpire(String token) {
		
		return extractExpiration(token).before(new Date());
		
		
		
	}

	private Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration);
	}
}
