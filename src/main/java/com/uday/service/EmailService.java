package com.uday.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(String to, String subject, String body) {
//
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		
		javaMailSender.send(message);
		
		System.out.println("Mail is sent to " + to);
		
		
//		MimeMessage message = javaMailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//		helper.setTo(to);
//		helper.setSubject(subject);
//		helper.setText(body, true);  // Enable HTML content
//
//		javaMailSender.send(message);
	}

}
