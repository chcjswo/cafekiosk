package me.mocadev.cafekiosk.spring.api.service.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.api.service.product.response.ProductResponse;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductRepository;
import me.mocadev.cafekiosk.spring.domain.product.ProductSellingStatus;
import org.springframework.stereotype.Service;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/
@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public List<ProductResponse> getSellingProduct() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
		return products.stream()
			.map(ProductResponse::of)
			.toList();
	}
}
