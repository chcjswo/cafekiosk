package me.mocadev.cafekiosk.spring.domain.order;

import me.mocadev.cafekiosk.spring.domain.orderproduct.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-25
 **/
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
