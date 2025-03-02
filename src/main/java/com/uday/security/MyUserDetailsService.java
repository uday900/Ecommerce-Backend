package com.uday.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uday.entity.User;
import com.uday.exception_handling.NotFoundException;
import com.uday.repository.UserRepository;


@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		
//		System.out.println("in custom function "+ username);
		
		User user = repository.findByEmail(username);

		if ( user == null ) {
			System.out.println("user not found ");
//			throw new UsernameNotFoundException("user not found "+ username);
			throw new NotFoundException("user not found with "+ username);
		}
		
//		User user = userOpt.get();
		System.out.println("user found with role " + user.getRole());

		
		
		return new UserPrincipal(user);
	}

}
