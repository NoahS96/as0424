package main.java.as0424.pojo.checkout;

import java.time.LocalDate;

import main.java.as0424.exceptions.CheckoutException;

public class Checkout {

	private String toolCode;
	private int rentalDayCount;
	private int discount;
	private LocalDate checkoutDate;
	
	
	/**
	 * 
	 * @param toolCode - Unique tool code/id
	 * @param rentalDaycount - Number of days requested to rent the tool 
	 * @param checkoutDate - The date of the checkout transaction
	 * @throws CheckoutException
	 */
	public Checkout(String toolCode, int rentalDaycount, LocalDate checkoutDate) throws CheckoutException {
		this(toolCode, rentalDaycount, 0, checkoutDate);
	}
	
	/**
	 * 
	 * @param toolCode - Unique tool code/id 
	 * @param rentalDayCount - Number of days requested to rent the tool 
	 * @param discount - Discount amount applied to rental charges. Integer representation of discount (ex. 20 is equivalent to 20%)
	 * @param checkoutDate - The date of the checkout transaction
	 * @throws CheckoutException 
	 */
	public Checkout(String toolCode, int rentalDayCount, int discount, LocalDate checkoutDate) throws CheckoutException {
		if (discount < 0 || discount > 100) {
			throw new CheckoutException("Discount must between 0 and 100");
		}
		if (rentalDayCount <= 0) {
			throw new CheckoutException("Rental day count must be 1 or greater");
		}
		
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
