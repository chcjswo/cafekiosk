package me.mocadev.cafekiosk.spring.domain.history.mail;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.mocadev.cafekiosk.spring.domain.product.BaseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MailSendHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fromEmail;
	private String toEmail;
	private String title;
	private String body;

	@Builder
	private MailSendHistory(String fromEmail, String toEmail, String title, String body) {
		this.fromEmail = fromEmail;
		this.toEmail = toEmail;
		this.title = title;
		this.body = body;
	}
}
