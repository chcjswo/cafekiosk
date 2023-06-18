package me.mocadev.cafekiosk.unit;

import static org.junit.jupiter.api.Assertions.*;
import me.mocadev.cafekiosk.unit.beverage.Americano;
import org.junit.jupiter.api.Test;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-06-19
 **/
class CafeKioskTest {

	@Test
	void add() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		System.out.println("cafeKiosk.getBeverages().size() = " + cafeKiosk.getBeverages().size());
	}
}
