package com.uday.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.uday.entity.User;
import com.uday.enums.UserRole;

import io.jsonwebtoken.lang.Arrays;

public class UserPrincipal implements UserDetails {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private User user;
	
	 public UserPrincipal(User user) {
	        this.user = user;
	 }


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Set<SimpleGrantedAuthority> authorities = new HashSet<>();

	    if (user.getRole() == UserRole.ADMIN) {
//	        System.out.println("Added role of admin");
	        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); 
	    }
	    
	    else authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
	    
	    return authorities;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
//		System.out.println("user name is "+ user.getEmail());
		// TODO Auto-generated method stub
		return user.getEmail();
	}

}
