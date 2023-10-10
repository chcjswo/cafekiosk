package me.mocadev.cafekiosk.spring.api.service.mail;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import me.mocadev.cafekiosk.spring.api.service.order.OrderStatisticsService;
import me.mocadev.cafekiosk.spring.client.mail.MailSendClient;
import me.mocadev.cafekiosk.spring.domain.history.mail.MailSendHistory;
import me.mocadev.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import me.mocadev.cafekiosk.spring.domain.order.Order;
import me.mocadev.cafekiosk.spring.domain.order.OrderProductRepository;
import me.mocadev.cafekiosk.spring.domain.order.OrderRepository;
import me.mocadev.cafekiosk.spring.domain.order.OrderStatus;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductRepository;
import me.mocadev.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MailServiceTest {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	MailSendHistoryRepository mailSendHistoryRepository;

	@MockBean
	private MailSendClient mailSendClient;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@DisplayName("결제 완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	@Test
	void sendMail() {
		// given
		Product product1 = createProduct(HANDMADE, "001", 4000);
		Product product2 = createProduct(HANDMADE, "002", 4500);
		Product product3 = createProduct(HANDMADE, "003", 7000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		String email = "aaa@aaa.com";

		LocalDateTime now = LocalDateTime.of(2023, 10, 10, 0, 0);
		Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 10, 9, 10, 0));
		Order order2 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 10, 11, 0, 0));
		Order order3 = createPaymentCompletedOrder(products, now);
		Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 10, 10, 23, 59, 59));

		// stubbing
		when(mailSendClient.sendMail(any(String.class), any(String.class), any(String.class), any(String.class)))
			.thenReturn(true);

		// when
		boolean result = orderStatisticsService.sendOrderStatistics(LocalDate.of(2023, 10, 10), email);

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
			.extracting("body")
			.contains("총 주문 금액: 46500원");
	}

	private Order createPaymentCompletedOrder( List<Product> products, LocalDateTime now) {
		Order order = Order.builder()
			.products(products)
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.registeredDateTime(now)
			.build();
		System.out.println("order = " + order);
		orderRepository.save(order);
		return order;
	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.name("메뉴")
			.price(price)
			.sellingStatus(SELLING)
			.type(type)
			.build();
	}
}
