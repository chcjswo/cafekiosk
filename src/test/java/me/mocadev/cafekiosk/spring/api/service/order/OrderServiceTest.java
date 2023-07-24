package me.mocadev.cafekiosk.spring.api.service.order;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import me.mocadev.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.order.response.OrderResponse;
import me.mocadev.cafekiosk.spring.domain.order.OrderProductRepository;
import me.mocadev.cafekiosk.spring.domain.order.OrderRepository;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductRepository;
import me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus;
import me.mocadev.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderService orderService;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}

	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrder() {
		// given
		Product product1 = createProduct(HANDMADE, "001", "아메리카노", 4000, SELLING);
		Product product2 = createProduct(HANDMADE, "002", "카페라떼", 4500, HOLD);
		Product product3 = createProduct(HANDMADE, "003", "팥빙수", 7000, NOT_SELLING);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(orderCreateRequest);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("002", 4500)
			);
	}

	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	@Test
	void test1() {
		// given
		Product product1 = createProduct(HANDMADE, "001", "아메리카노", 4000, SELLING);
		Product product2 = createProduct(HANDMADE, "002", "카페라떼", 4500, HOLD);
		Product product3 = createProduct(HANDMADE, "003", "팥빙수", 7000, NOT_SELLING);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(orderCreateRequest);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("001", 4000)
			);
	}

	private Product createProduct(ProductType type, String productNumber, String name, int price, ProductSellingStatus sellingStatus) {
		return Product.builder()
			.productNumber(productNumber)
			.name(name)
			.price(price)
			.sellingStatus(sellingStatus)
			.type(type)
			.build();
	}
}
