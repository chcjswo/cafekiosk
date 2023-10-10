package me.mocadev.cafekiosk.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.api.service.mail.MailService;
import me.mocadev.cafekiosk.spring.domain.order.Order;
import me.mocadev.cafekiosk.spring.domain.order.OrderRepository;
import me.mocadev.cafekiosk.spring.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	public boolean sendOrderStatistics(LocalDate orderDate, String email) {
		List<Order> orders = orderRepository.findOrdersBy(
			orderDate.atStartOfDay(),
			orderDate.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		int totalAmount = orders.stream()
			.mapToInt(Order::getTotalPrice)
			.sum();

		boolean result = mailService.sendMail("no-reply@test.com",
			email,
			"주문 통계",
			"총 주문 금액: " + totalAmount + "원");

		if (!result) {
			throw new IllegalArgumentException("메일 전송 실패");
		}
		return true;
	}
}
