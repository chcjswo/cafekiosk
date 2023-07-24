package me.mocadev.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.mocadev.cafekiosk.spring.domain.orderproduct.OrderProduct;
import me.mocadev.cafekiosk.spring.domain.product.BaseEntity;
import me.mocadev.cafekiosk.spring.domain.product.Product;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-24
 **/
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int totalPrice;

	private LocalDateTime registeredDateTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public Order(List<Product> products) {
		this.orderStatus = OrderStatus.INIT;
		this.totalPrice = getTotalPrice(products);
		this.registeredDateTime = LocalDateTime.now();
		this.orderProducts = products.stream()
			.map(product -> new OrderProduct(this, product))
			.toList();
	}

	public static Order create(List<Product> products) {
		return new Order(products);
	}

	private int getTotalPrice(List<Product> products) {
		return products.stream()
			.mapToInt(Product::getPrice)
			.sum();
	}
}
