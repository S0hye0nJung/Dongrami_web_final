package com.lec.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lec.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendTempPassword(String to, String tempPassword) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("0506ebxao@naver.com");
		message.setTo(to);
		message.setSubject("임시 비빌번호 발급");
		message.setText("저희 동그라미 서비스를 이용해주셔서 감사합니다.");
		message.setText("귀하의 임시 비밀번호는 " + tempPassword + " 입니다.");
		
		mailSender.send(message);
		
	}

}
