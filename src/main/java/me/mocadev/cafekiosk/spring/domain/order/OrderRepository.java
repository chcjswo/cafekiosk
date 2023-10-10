package me.mocadev.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-25
 **/
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select o from Order o where o.registeredDateTime >= :startDateTime and o.registeredDateTime <= :endDateTime " +
		"and o.orderStatus = :orderStatus")
    List<Order> findOrdersBy(@Param("startDateTime") LocalDateTime startDateTime,
							 @Param("endDateTime") LocalDateTime endDateTime,
							 @Param("orderStatus") OrderStatus orderStatus);
}
