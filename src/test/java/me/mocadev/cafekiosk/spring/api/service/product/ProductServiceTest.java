package me.mocadev.cafekiosk.spring.api.service.product;

import static me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static me.mocadev.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import java.util.List;
import me.mocadev.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.product.response.ProductResponse;
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

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
	@Test
	void createProduct() {
		// given
		Product product = createProduct("001", "아메리카노", 1000, SELLING, HANDMADE);
		productRepository.save(product);

		ProductCreateRequest request = ProductCreateRequest.builder()
			.name("아이스 티")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.price(3000)
			.build();

		// when
		ProductResponse response = productService.createProduct(request);

		// then
		assertThat(response)
			.extracting("productNumber", "name", "price", "sellingStatus", "type")
			.contains("002", "아이스 티", 3000, SELLING, HANDMADE);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "price", "sellingStatus", "type")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", 1000, SELLING, HANDMADE),
				tuple("002", "아이스 티", 3000, SELLING, HANDMADE)
			);
	}
	@DisplayName("신규 상품을 등록 시 상품이 없는 경우는 상품번호는 001이다.")
	@Test
	void createProduct1() {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.name("아이스 티")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.price(3000)
			.build();

		// when
		ProductResponse response = productService.createProduct(request);

		// then
		assertThat(response)
			.extracting("productNumber", "name", "price", "sellingStatus", "type")
			.contains("001", "아이스 티", 3000, SELLING, HANDMADE);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(1)
			.extracting("productNumber", "name", "price", "sellingStatus", "type")
			.contains(
				tuple("001", "아이스 티", 3000, SELLING, HANDMADE)
			);
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
