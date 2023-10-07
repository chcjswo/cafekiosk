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
@Getter
@RequiredArgsConstructor
public enum ProductType {

	HANDMADE("핸드메이드"),
	BOTTLE("병 음료"),
	BAKERY("베이커리");

	private final String text;

	public static boolean containsStockType(ProductType type) {
		return List.of(BOTTLE, BAKERY).contains(type);
	}
}
