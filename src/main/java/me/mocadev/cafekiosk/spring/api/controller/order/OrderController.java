package me.mocadev.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import me.mocadev.cafekiosk.spring.api.ApiResponse;
import me.mocadev.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import me.mocadev.cafekiosk.spring.api.service.order.OrderService;
import me.mocadev.cafekiosk.spring.api.service.order.response.OrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders/new")
	public ApiResponse<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
		return ApiResponse.ok(orderService.createOrder(request.toServiceRequest()));
	}
}
