package me.mocadev.cafekiosk.unit.beverage;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2023-06-19
 **/
public class Latte implements Beverage {
	@Override
	public String getName() {
		return "라떼";
	}

	@Override
	public int getPrice() {
		return 4500;
	}
}
