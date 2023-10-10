package me.mocadev.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.client.mail.MailSendClient;
import me.mocadev.cafekiosk.spring.domain.history.mail.MailSendHistory;
import me.mocadev.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

	private final MailSendClient mailSendClient;
	private final MailSendHistoryRepository mailSendHistoryService;

	public boolean sendMail(String fromEmail, String toEmail, String title, String body) {
		boolean result = mailSendClient.sendMail(fromEmail, toEmail, title, body);
		if (result) {
			mailSendHistoryService.save(MailSendHistory.builder()
				.fromEmail(fromEmail)
				.toEmail(toEmail)
				.title(title)
				.body(body)
				.build());
			return true;
		}
		return false;
	}
}
