package me.mocadev.cafekiosk.spring.api.service.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.order.response.OrderResponse;
import me.mocadev.cafekiosk.spring.domain.order.Order;
import me.mocadev.cafekiosk.spring.domain.order.OrderRepository;
import me.mocadev.cafekiosk.spring.domain.product.Product;
import me.mocadev.cafekiosk.spring.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
		List<String> productNumbers = orderCreateRequest.getProductNumbers();
		List<Product> list = findProductsBy(productNumbers);

		Order order = Order.create(list);
		Order savedOrder = orderRepository.save(order);

		return OrderResponse.of(savedOrder);
	}

	private List<Product> findProductsBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, product -> product));
		return productNumbers.stream()
			.map(productMap::get)
			.toList();
	}
}
