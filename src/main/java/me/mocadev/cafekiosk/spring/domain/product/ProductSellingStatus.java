package me.mocadev.cafekiosk.spring.domain.product;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/
@RequiredArgsConstructor
@Getter
public enum ProductSellingStatus {

	SELLING("판매"),
	HOLD("판매 보류"),
	NOT_SELLING("판매 안함");

	private final String text;

	public static List<ProductSellingStatus> forDisplay() {
		return List.of(SELLING, HOLD);
	}
}
