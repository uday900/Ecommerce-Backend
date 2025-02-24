package com.uday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.uday.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

//	User findByEmail(String email);

	User findByResetPasswordToken(String resetPasswordToken);

//	@Query("select user from User user where email=:email")
	User findByEmail(String email);

}
