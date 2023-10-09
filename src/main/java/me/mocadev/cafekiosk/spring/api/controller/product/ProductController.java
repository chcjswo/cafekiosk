package me.mocadev.cafekiosk.spring.api.controller.product;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.api.ApiResponse;
import me.mocadev.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.product.ProductService;
import me.mocadev.cafekiosk.spring.api.service.product.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/
@RequiredArgsConstructor
@RestController
public class ProductController {

	private final ProductService productService;

	@PostMapping("/api/v1/products")
	public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
		return ApiResponse.ok(productService.createProduct(request.toServiceRequest()));
	}

	@GetMapping("/api/v1/products/selling")
	public ApiResponse<List<ProductResponse>> getSellingProduct() {
		return ApiResponse.ok(productService.getSellingProduct());
	}
}
