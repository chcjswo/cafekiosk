package me.mocadev.cafekiosk.spring.api.service.order;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
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

	/**
	 * 재고 감소 -> 동시성 문제
	 * optimistic lock / pessimistic lock
	 */
	@Transactional
	public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
		List<String> productNumbers = orderCreateRequest.getProductNumbers();
		List<Product> products = findProductsBy(productNumbers);

		deductStockQuantity(products);

		Order order = Order.create(products);
		Order savedOrder = orderRepository.save(order);

		return OrderResponse.of(savedOrder);
	}

	private void deductStockQuantity(List<Product> products) {
		// 재고 차감 체크가 필요한 상품들 filter
		List<String> stockProductNumbers = extractStockProductNumbers(products);

		// 재고 엔티티 조회
		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

		// 상품별 counting
		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		// 재고 차감
		for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();
			if (stock.isNotEnoughStock(quantity)) {
				throw new IllegalArgumentException(stockProductNumber + " 재고가 부족합니다.");
			}
			stock.deductStock(quantity);
		}
	}

	private List<String> extractStockProductNumbers(List<Product> products) {
		return products.stream()
			.filter(product -> ProductType.containsStockType(product.getType()))
			.map(Product::getProductNumber)
			.toList();
	}

	private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		return stocks.stream()
			.collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
	}

	private Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
			.collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));
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
