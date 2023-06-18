package me.mocadev.cafekiosk.unit;

import me.mocadev.cafekiosk.unit.beverage.Americano;
import me.mocadev.cafekiosk.unit.beverage.Latte;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-06-19
 **/
public class CafeKioskRunner {

	public static void main(String[] args) {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());
		System.out.println(">>> 아메리카노 추가");

		cafeKiosk.add(new Latte());
		System.out.println(">>> 라떼 추가");

		int totalPrice = cafeKiosk.calculateTotalPrice();
		System.out.println("totalPrice = " + totalPrice);
	}
}
