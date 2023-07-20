package me.mocadev.cafekiosk.spring.domain.product;

import lombok.RequiredArgsConstructor;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/
@RequiredArgsConstructor
public enum ProductType {

	HANDMADE("핸드메이드"),
	BOTTLE("병 음료"),
	BAKERY("베이커리");

	private final String text;

}
