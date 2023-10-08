package me.mocadev.cafekiosk.spring.domain.product;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-21
 **/
@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("원하는 판매 상태의 상품을 조회한다.")
	@Test
	void findAllBySellingStatusIn() {
		// given
		Product product1 = createProduct("001", "아메리카노", 1000, SELLING, HANDMADE);
		Product product2 = createProduct("002", "카페라테", 4000, HOLD, HANDMADE);
		productRepository.saveAll(List.of(product1, product2));

		// when
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

		// then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", SELLING),
				tuple("002", "카페라테", HOLD)
			);
	}

	@DisplayName("상품번호 리스트로 상품들을 조회한다.")
	@Test
	void findAllByProductNumberIn() {
		// given
		Product product1 = createProduct("001", "아메리카노", 1000, SELLING, HANDMADE);
		Product product2 = createProduct("002", "카페라떼", 4500, HOLD, HANDMADE);
		Product product3 = createProduct("003", "팥빙수", 7000, NOT_SELLING, HANDMADE);
		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

		// then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", SELLING),
				tuple("002", "카페라떼", HOLD)
			);
	}

	@DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 조회한다.")
	@Test
	void findLatestProduct() {
		// given
		String targetProductNumber = "003";
		Product product1 = createProduct("001", "아메리카노", 1000, SELLING, HANDMADE);
		Product product2 = createProduct("002", "카페라테", 4500, HOLD, HANDMADE);
		Product product3 = createProduct(targetProductNumber, "팥빙수", 7000, NOT_SELLING, HANDMADE);
		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		String latestProductNumber = productRepository.findLatestProductNumber();

		// then;
		assertThat(latestProductNumber).isEqualTo(targetProductNumber);
	}

	@DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 조회할 때, 저장된 상품이 없으면 null을 반환한다.")
	@Test
	void findLatestProduct2() {
		// when
		String latestProductNumber = productRepository.findLatestProductNumber();

		// then
		assertThat(latestProductNumber).isNull();
	}

	private Product createProduct(String productNumber, String name, int price, ProductSellingStatus sellingStatus, ProductType type) {
		return Product.builder()
			.productNumber(productNumber)
			.name(name)
			.price(price)
			.sellingStatus(sellingStatus)
			.type(type)
			.build();
	}
}
