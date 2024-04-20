package main.java.as0424.pojo.checkout;

import java.time.LocalDate;

public class Checkout {

	private String toolCode;
	private int rentalDayCount;
	private int discount;
	private LocalDate checkoutDate;
	
	
	/**
	 * 
	 * @param toolCode - Unique tool code/id
	 * @param rentalDaycount - Number of days requested to rent the tool 
	 */
	public Checkout(String toolCode, int rentalDaycount, LocalDate checkoutDate) {
		this(toolCode, rentalDaycount, 0, checkoutDate);
	}
	
	/**
	 * 
	 * @param toolCode - Unique tool code/id
	 * @param rentalDayCount - Number of days requested to rent the tool 
	 * @param discount - Discount amount applied to rental charges. Integer representation of discount (ex. 20 is equivalent to 20%)
	 */
	public Checkout(String toolCode, int rentalDayCount, int discount, LocalDate checkoutDate) {
		this.setToolCode(toolCode);
		this.setRentalDayCount(rentalDayCount);
		this.setDiscount(discount);
		this.setCheckoutDate(checkoutDate);
	}

	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}

	public int getRentalDayCount() {
		return rentalDayCount;
	}

	public void setRentalDayCount(int rentalDayCount) {
		this.rentalDayCount = rentalDayCount;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
}
