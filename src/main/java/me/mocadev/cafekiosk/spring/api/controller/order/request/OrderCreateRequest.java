package me.mocadev.cafekiosk.spring.api.controller.order.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@Getter
@NoArgsConstructor
public class OrderCreateRequest {

	private List<String> productNumbers;

	@Builder
	private OrderCreateRequest(List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}
}
