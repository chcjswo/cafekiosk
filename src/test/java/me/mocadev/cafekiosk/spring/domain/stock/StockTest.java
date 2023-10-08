package me.mocadev.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockTest {

	@DisplayName("재고가 충분하면 true를 반환한다.")
	@Test
	void isNotEnoughStock() {
		// given
		Stock stock = Stock.create("001", 1);
		int quantity = 2;

		// when
		boolean result = stock.isNotEnoughStock(quantity);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("재고를 감소시킨다.")
	@Test
	void decreaseStock() {
		// given
		Stock stock = Stock.create("001", 1);
		int quantity = 1;

		// when
		stock.deductStock(quantity);

		// then
		assertThat(stock.getQuantity()).isZero();
	}

	@DisplayName("재고보다 많은 수량으로 재고를 감소시키면 예외가 발생한다.")
	@Test
	void decreaseStock2() {
		// given
		Stock stock = Stock.create("001", 1);
		int quantity = 2;

		// when
		// then
		assertThatThrownBy(() -> stock.deductStock(quantity))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("재고 수량이 부족합니다.");
	}
}
