package me.mocadev.cafekiosk.spring.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@Getter
@RequiredArgsConstructor
public enum OrderStatus {

	INIT("주문생성"),
	CANCELED("주문취소"),
	PAYMENT_COMPLETED("결제완료"),
	PAYMENT_FAILED("결제실패"),
	RECEIVED("주문접수"),
	COMPLETED("처리완료");

	private final String text;
}
