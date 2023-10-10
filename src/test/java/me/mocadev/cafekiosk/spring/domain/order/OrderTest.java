package me.mocadev.cafekiosk.spring.domain.order;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus;
import me.mocadev.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
class OrderTest {

	@DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
	@Test
	void calculateTotalPrice() {
		// given
		List<Product> products = List.of(
			createProduct(HANDMADE, "001", "아메리카노1", 4000, SELLING),
			createProduct(HANDMADE, "002", "아메리카노2", 3000, SELLING)
		);

		// when
		Order order = Order.create(products, LocalDateTime.now());

		// then
		assertThat(order.getTotalPrice()).isEqualTo(7000);
	}

	@DisplayName("주문 생성 시 주문 상태는 INIT이다.")
	@Test
	void init() {
		// given
		List<Product> products = List.of(
			createProduct(HANDMADE, "001", "아메리카노1", 4000, SELLING),
			createProduct(HANDMADE, "002", "아메리카노2", 3000, SELLING)
		);

		// when
		Order order = Order.create(products, LocalDateTime.now());

		// then
		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
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
