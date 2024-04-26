package main.java.as0424.pojo.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.java.as0424.enums.HolidayEnum;
import main.java.as0424.pojo.tool.Tool;

public class RentalAgreement {

	private Tool tool;
	private Checkout checkout;
	private LocalDate dueDate;
	private int chargeableDays;
	private BigDecimal preDiscountCharge;
	private BigDecimal discountAmount;
	private BigDecimal finalCharge;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YY");
	
	
	/**
	 * 
	 * @param tool - Tool 
	 * @param checkout - Checkout
	 */
	public RentalAgreement(Tool tool, Checkout checkout, LocalDate dueDate, int chargeableDays, BigDecimal preDiscountCharge, 
			BigDecimal discountAmount, BigDecimal finalCharge) {
		this.tool = tool;
		this.checkout = checkout;
		this.dueDate = dueDate;
		this.chargeableDays = chargeableDays;
		this.preDiscountCharge = preDiscountCharge;
		this.discountAmount = discountAmount;
		this.finalCharge = finalCharge; 
	}
 

	public Tool getTool() {
		return tool;
	}


	public Checkout getCheckout() {
		return checkout;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	
	public int getChargeableDays() {
		return chargeableDays;
	}
	
	public BigDecimal getPreDiscountCharge() {
		return preDiscountCharge;
	}
	
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	
	public BigDecimal getFinalCharge() {
		return finalCharge;
	}
	
	/**
	 * Converts the input into a $ formatted string (ex. $1,065.00)
	 * @param decimal - BigDecimal
	 * @return String - dollar formatted string
	 */
	private String getCurrencyFormattedDecimal(BigDecimal decimal) {
		return NumberFormat.getCurrencyInstance(Locale.US).format(decimal);
	}
	
	private String getCurrencyFormattedDecimal(double number) {
		return getCurrencyFormattedDecimal(new BigDecimal(number));
	}
	
	private String getFormattedDate(LocalDate date) {
		return formatter.format(date);
	}
	
	public void printAgreement() {
		System.out.println("Tool Code: " + tool.getToolCode());
		System.out.println("Tool Type: " + tool.getToolType().name);
		System.out.println("Tool Brand: " + tool.getBrand());
		System.out.println("Rental Days: " + checkout.getRentalDayCount());
		System.out.println("Check Out Date: " + getFormattedDate(checkout.getCheckoutDate())); // Need to format this
		System.out.println("Due Date: " + getFormattedDate(dueDate)); // Need to format this
		System.out.println("Daily Rental Charge: " + getCurrencyFormattedDecimal(tool.getToolType().dailyCharge));
		System.out.println("Charge Days: " + chargeableDays);
		System.out.println("Pre-Discount Charge: " + getCurrencyFormattedDecimal(preDiscountCharge));
		System.out.println("Discount Percent: " + checkout.getDiscount() + "%");
		System.out.println("Discount Amount: " + getCurrencyFormattedDecimal(discountAmount));
		System.out.println("Final Charge: " + getCurrencyFormattedDecimal(finalCharge));
		System.out.println();
	}
}
