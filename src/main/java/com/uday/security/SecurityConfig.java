package com.uday.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.uday.enums.UserRole;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private JWTFilter jwtFilter;
	
	@Bean
	SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				
				// Cross-Site Request Forgery
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
										
				.authorizeHttpRequests( req -> req
						
						.requestMatchers("public/**","auth/**",
								"/carousels/fetch",
								"/category/fetch/**", //"/categories/fetch/{categoryId}",
								"/products/fetch/**"
								
								).permitAll()
//		                .requestMatchers("/carousels/**" ).hasRole("ADMIN")  //, "/carousels/delete/**").hasRole("ADMIN") // Restricted to ADMIN
						
						.anyRequest().authenticated()
						)
				.sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				
				.httpBasic(Customizer.withDefaults())
				
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		authenticationProvider.setUserDetailsService(userDetailsService);
		
		return authenticationProvider;
	}

}
