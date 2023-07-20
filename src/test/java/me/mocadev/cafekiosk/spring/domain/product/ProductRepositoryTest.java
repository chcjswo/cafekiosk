package me.mocadev.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
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
		Product product1 = Product.builder()
			.productNumber("001")
			.name("아메리카노")
			.price(1000)
			.sellingStatus(ProductSellingStatus.SELLING)
			.type(ProductType.HANDMADE)
			.build();
		Product product2 = Product.builder()
			.productNumber("002")
			.name("카페라테")
			.price(4000)
			.sellingStatus(ProductSellingStatus.HOLD)
			.type(ProductType.HANDMADE)
			.build();
		productRepository.saveAll(List.of(product1, product2));

		// when
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

		// then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", ProductSellingStatus.SELLING),
				tuple("002", "카페라테", ProductSellingStatus.HOLD)
			);
	}
}
