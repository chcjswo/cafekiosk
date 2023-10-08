package me.mocadev.cafekiosk.spring.domain.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.mocadev.cafekiosk.spring.domain.product.BaseEntity;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	private int quantity;

	@Builder
	private Stock(String productNumber, int quantity) {
		this.productNumber = productNumber;
		this.quantity = quantity;
	}

	public static Stock create(String number, int quantity) {
		return Stock.builder()
			.productNumber(number)
			.quantity(quantity)
			.build();
	}

	public boolean isNotEnoughStock(int quantity) {
		return this.quantity < quantity;
	}

	public void deductStock(int quantity) {
		if (isNotEnoughStock(quantity)) {
			throw new IllegalArgumentException("재고 수량이 부족합니다.");
		}
		this.quantity -= quantity;
	}
}
