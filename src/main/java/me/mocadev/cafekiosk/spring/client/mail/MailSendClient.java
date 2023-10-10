package me.mocadev.cafekiosk.spring.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {

	public boolean sendMail(String fromEmail, String toEmail, String title, String body) {
		log.info("메일 전송 fromEmail: {}, toEmail: {}, title: {}, body: {}", fromEmail, toEmail, title, body);
		throw new IllegalArgumentException("메일 전송");
	}
}
