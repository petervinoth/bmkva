package com.bm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailSenter")
public class EmailSenter {
	@Autowired
	private JavaMailSender javaMailSender;
	
	public EmailSenter(JavaMailSender javaMailSender) {
		this.javaMailSender=javaMailSender;
	}
	
	public void sendEmail(SimpleMailMessage email) {
		
		javaMailSender.send(email);
	}

}
