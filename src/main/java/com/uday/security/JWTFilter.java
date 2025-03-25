package com.uday.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uday.exception_handling.InvalidSessionOrToken;
import com.uday.service.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraWxsIiwiaWF0IjoxNzIzMTgzNzExLCJleHAiOjE3MjMxODM4MTl9.5nf7dRzKRiuGurN2B9dHh_M5xiu73ZzWPr6rbhOTTHs
		
		String authHeader = request.getHeader("Authorization");
		
		String token = null;
		String username = null;
		
		if ( authHeader != null && authHeader.startsWith("Bearer ")) {
			
			token = authHeader.substring(7);
			
			// Check if token is blacklisted
            if (tokenBlacklistService.isBlacklisted(token)) {
            	throw new RuntimeException("Token has been invalidated.");
            }
            
            
            try {
                
            	username = jwtService.extractUserName(token);
            } catch (InvalidSessionOrToken e) {
            	
                throw e; // Ensure Spring's global exception handler catches it
            }
			
			
			
		}
		// checking user is not empty and not already loggined in security context
		if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			
			UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class)
					.loadUserByUsername(username);
			
			if ( jwtService.isValidToken( userDetails, token)) {
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()	);
				
				authToken.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				throw new InvalidSessionOrToken("Invalid or expired token. Please log in again.");
			}	
			
		}
		filterChain.doFilter(request, response);
		
	}

}
