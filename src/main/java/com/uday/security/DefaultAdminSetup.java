package com.uday.security;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.uday.entity.User;
import com.uday.enums.UserRole;
import com.uday.repository.UserRepository;
@Component
public class DefaultAdminSetup implements CommandLineRunner {

	@Autowired
	private UserRepository repository;
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		String adminEmail = "admin@example.com";

        if (repository.findByEmail(adminEmail) == null ) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(bCryptPasswordEncoder.encode("admin123")); // Encrypted password
            admin.setRole(UserRole.ADMIN);

            repository.save(admin);
            System.out.println("✅ Default admin user created: " + adminEmail);
        } 
//        else {
//            System.out.println("🔹 Admin user already exists.");
//        }
	}

}
