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
	public RentalAgreement(Tool tool, Checkout checkout) {
		this.tool = tool;
		this.checkout = checkout;
		this.dueDate = calculateDueDate();
		this.chargeableDays = calculateChargeableDays();
		this.preDiscountCharge = calculatePreDiscountCharge();
		this.discountAmount = calculateDiscountAmount();
		this.finalCharge = calculateFinalCharge(); 
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
	 * Take the checkout date and add the rental day count. Subtract 1 to account for the charge on the rental day.
	 * 01/01/2024 plus 5 days is 01/06/2024 however this is exclusive of the charge on the rental day. By subtracting
	 * 1 this is inclusive of the rental day. This should be clarified in the requirements.
	 * @return LocalDate - A representation of the due date
	 */
	private LocalDate calculateDueDate() {
		return  checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount() - 1);
	}
	
	/**
	 * Calculate the total number of chargeable days based on the ToolType selected, checkout date, and due date. 
	 * @return int - The number of chargeable days
	 */
	private int calculateChargeableDays() {
		// Calculate total days between checkout and due date then subtract the 
		// non-chargeable days.
		int weekdayCount = 0;
		int weekendDayCount = 0;
		int holidayCount = 0; 
		int totalChargeableDays = 0;
		
		List<LocalDate> holidays = Stream.of(HolidayEnum.values())
				.map(holiday -> holiday.getObservableDate(checkout.getCheckoutDate().getYear()))
				.collect(Collectors.toList());
		LocalDate date = checkout.getCheckoutDate();
		
		// Check if holidays fall between dates. Sub 1 from start and add 1 to end to check if start or end are holidays.
		for (LocalDate holiday : holidays) {
			if (holiday.isAfter(date.minusDays(1)) && holiday.isBefore(this.dueDate.plusDays(1))) {
				holidayCount++; 
			}
		}
		
		// From the checkout date to our due date plus 1 (to make sure we charge on the due date) we
		// get the count of weekdays and weekends.
		while (date.isBefore(this.dueDate.plusDays(1))) {
			DayOfWeek dayOfWeek = date.getDayOfWeek();
			if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
				weekendDayCount++;
			} else {
				weekdayCount++;
			}
			date = date.plusDays(1);
		}
		
		// Check if weekdays or weekends are chargeable
		if (tool.getToolType().hasWeekdayCharge) {
			totalChargeableDays += weekdayCount;
		}
		if (tool.getToolType().hasWeekendCharge) {
			totalChargeableDays += weekendDayCount;
		}
		
		// The weekday / weekend count will already have holidays within the count if there are any.
		// If holidays are not charged then we need to remove them from the total count.
		if (!tool.getToolType().hasHolidayCharge) {
			totalChargeableDays -= holidayCount;
		} 
		
		totalChargeableDays = totalChargeableDays >= 0 ? totalChargeableDays : 0;
		
		return totalChargeableDays;
	}
	
	/**
	 * Calculate the pre-discount charge by multiplying tool daily rate by number of chargeable days.
	 * @return BigDecimal - The pre-discount charge
	 */
	private BigDecimal calculatePreDiscountCharge() {
		BigDecimal dailyCharge = new BigDecimal(tool.getToolType().dailyCharge);
		
		return dailyCharge.multiply(new BigDecimal(this.chargeableDays)).setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Calculate the discount amount by multiply the decimal percentage to the pre-discount charge.
	 * @return BigDecimal - The discount amount
	 */
	private BigDecimal calculateDiscountAmount() {
		BigDecimal discountPercent = new BigDecimal(checkout.getDiscount() * 0.01);
		
		return discountPercent.multiply(this.preDiscountCharge).setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Calculate the final charge by subtracting the discount amount from the pre-discount amount
	 * @return
	 */
	private BigDecimal calculateFinalCharge() {
		return preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
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
