package me.mocadev.cafekiosk.spring.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	@Enumerated(EnumType.STRING)
	private ProductType type;

	@Enumerated(EnumType.STRING)
	private ProductSellingStatus sellingStatus;

	private String name;

	private int price;

	@Builder
	private Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
		this.productNumber = productNumber;
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}
}
