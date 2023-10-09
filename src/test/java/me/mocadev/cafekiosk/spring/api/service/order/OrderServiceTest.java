package me.mocadev.cafekiosk.spring.api.service.order;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import jakarta.transaction.Transactional;
import me.mocadev.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import me.mocadev.cafekiosk.spring.api.service.order.response.OrderResponse;
import me.mocadev.cafekiosk.spring.domain.order.OrderProductRepository;
import me.mocadev.cafekiosk.spring.domain.order.OrderRepository;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductRepository;
import me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus;
import me.mocadev.cafekiosk.spring.domain.product.ProductType;
import me.mocadev.cafekiosk.spring.domain.stock.Stock;
import me.mocadev.cafekiosk.spring.domain.stock.StockRepository;
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
	private StockRepository stockRepository;

	@Autowired
	private OrderService orderService;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrder() {
		// given
		Product product1 = createProduct(HANDMADE, "001", "아메리카노", 4000, SELLING);
		Product product2 = createProduct(HANDMADE, "002", "카페라떼", 4500, HOLD);
		Product product3 = createProduct(HANDMADE, "003", "팥빙수", 7000, NOT_SELLING);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(request);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("002", 4500)
			);
	}

	@DisplayName("재고가 존재하는 상품에 대한 주문번호 리스트를 받아 주문을 생성한다.")
	@Test
	void createOrderWithStock() {
		// given
		Product product1 = createProduct(BOTTLE, "001", "아메리카노", 4000, SELLING);
		Product product2 = createProduct(BAKERY, "002", "카페라떼", 4500, HOLD);
		Product product3 = createProduct(HANDMADE, "003", "팥빙수", 7000, NOT_SELLING);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);

		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(request);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("totalPrice")
			.isEqualTo(19500);
		assertThat(orderResponse.getProducts()).hasSize(4)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("001", 4000),
				tuple("002", 4500),
				tuple("003", 7000)
			);

		List<Stock> stocks = stockRepository.findAll();
		assertThat(stocks).hasSize(2)
			.extracting("productNumber", "quantity")
			.containsExactlyInAnyOrder(
				tuple("001", 0),
				tuple("002", 1)
			);
	}

	@DisplayName("재고가 부족한 상품으로 주문을 생성하면 예외가 발생한다.")
	@Test
	void createOrderWithStock2() {
		// given
		Product product1 = createProduct(BOTTLE, "001", "아메리카노", 4000, SELLING);
		Product product2 = createProduct(BAKERY, "002", "카페라떼", 4500, HOLD);
		Product product3 = createProduct(HANDMADE, "003", "팥빙수", 7000, NOT_SELLING);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 1);
		Stock stock2 = Stock.create("002", 2);

		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();

		// when
		// then
		assertThatThrownBy(() -> orderService.createOrder(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("001 재고가 부족합니다.");
	}

	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	@Test
	void test1() {
		// given
		Product product1 = createProduct(HANDMADE, "001", "아메리카노", 4000, SELLING);
		Product product2 = createProduct(HANDMADE, "002", "카페라떼", 4500, HOLD);
		Product product3 = createProduct(HANDMADE, "003", "팥빙수", 7000, NOT_SELLING);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(request);

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
