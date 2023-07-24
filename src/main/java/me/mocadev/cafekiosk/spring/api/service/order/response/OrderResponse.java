package me.mocadev.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import me.mocadev.cafekiosk.spring.api.service.product.response.ProductResponse;
import me.mocadev.cafekiosk.spring.domain.order.Order;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@Getter
public class OrderResponse {

	private Long id;
	private int totalPrice;
	private LocalDateTime registeredDateTime;
	private List<ProductResponse> products;

	@Builder
	private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.registeredDateTime = registeredDateTime;
		this.products = products;
	}

	public static OrderResponse of(Order order) {
		return OrderResponse.builder()
			.id(order.getId())
			.totalPrice(order.getTotalPrice())
			.registeredDateTime(order.getRegisteredDateTime())
			.products(order.getOrderProducts().stream()
				.map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
				.toList())
			.build();
	}
}
