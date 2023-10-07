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
import me.mocadev.cafekiosk.spring.domain.product.ProductType;
import me.mocadev.cafekiosk.spring.domain.stock.Stock;
import me.mocadev.cafekiosk.spring.domain.stock.StockRepository;
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
	private final StockRepository stockRepository;

	public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
		List<String> productNumbers = orderCreateRequest.getProductNumbers();
		List<Product> products = findProductsBy(productNumbers);

		// 재고 차감 체크가 필요한 상품들 filter
		List<String> stockProductNumbers = products.stream()
			.filter(product -> ProductType.containsStockType(product.getType()))
			.map(Product::getProductNumber)
			.toList();
		// 재고 엔티티 조회
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

		// 상품별 counting
		// 재고 차감

		Order order = Order.create(products);
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
