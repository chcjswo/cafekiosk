package me.mocadev.cafekiosk.spring.api.service.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.product.response.ProductResponse;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductRepository;
import me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public ProductResponse createProduct(ProductCreateRequest request) {
		final String nextProductNumber = createNextProductNumber();
		final Product product = request.toEntity(nextProductNumber);
		final Product savedProduct = productRepository.save(product);

		productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProduct() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
		return products.stream()
			.map(ProductResponse::of)
			.toList();
	}

	private String createNextProductNumber() {
		final String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}
		final int productNumber = Integer.parseInt(latestProductNumber);
		final int nextProductNumber = productNumber + 1;
		return String.format("%03d", nextProductNumber);
	}
}
