package me.mocadev.cafekiosk.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-07-20
 **/
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

	List<Product> findAllByProductNumberIn(List<String> productNumbers);

	@Query(value = "select p.product_number from Product p order by id desc limit 1", nativeQuery = true)
	String findLatestProductNumber();
}
