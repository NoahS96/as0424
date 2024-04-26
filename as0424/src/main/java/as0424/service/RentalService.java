package main.java.as0424.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.java.as0424.enums.HolidayEnum;
import main.java.as0424.pojo.checkout.Checkout;
import main.java.as0424.pojo.checkout.RentalAgreement;
import main.java.as0424.pojo.tool.Tool;

public class RentalService {

	
	public RentalAgreement generateAgreement(Tool tool, Checkout checkout) {
		LocalDate dueDate = calculateDueDate(checkout);
		int chargeableDays = calculateChargeableDays(checkout, tool, dueDate);
		BigDecimal preDiscountCharge = calculatePreDiscountCharge(tool, chargeableDays);
		BigDecimal discountAmount = calculateDiscountAmount(checkout, preDiscountCharge);
		BigDecimal finalCharge = calculateFinalCharge(preDiscountCharge, discountAmount); 
		
		return new RentalAgreement(tool, checkout, dueDate, chargeableDays, preDiscountCharge, discountAmount, finalCharge);
	}

	/**
	 * Take the checkout date and add the rental day count. Subtract 1 to account for the charge on the rental day.
	 * 01/01/2024 plus 5 days is 01/06/2024 however this is exclusive of the charge on the rental day. By subtracting
	 * 1 this is inclusive of the rental day. This should be clarified in the requirements.
	 * @param checkout - Checkout object containing tool and rental length info
	 * @return LocalDate - A representation of the due date
	 */
	private LocalDate calculateDueDate(Checkout checkout) {
		return  checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount() - 1);
	}
	
	/**
	 * Calculate the total number of chargeable days based on the ToolType selected, checkout date, and due date. 
	 * @param checkout - Checkout object containing tool and rental length info
	 * @param tool - Tool object containing pricing info
	 * @param dueDate - LocalDate representation of rental end date
	 * @return int - The number of chargeable days
	 */
	private int calculateChargeableDays(Checkout checkout, Tool tool, LocalDate dueDate) {
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
			if (holiday.isAfter(date.minusDays(1)) && holiday.isBefore(dueDate.plusDays(1))) {
				holidayCount++; 
			}
		}
		
		// From the checkout date to our due date plus 1 (to make sure we charge on the due date) we
		// get the count of weekdays and weekends.
		while (date.isBefore(dueDate.plusDays(1))) {
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
	 * @param tool - Tool object containing pricing info
	 * @param chargeableDays - int of the number of days the customer will be charged for
	 * @return BigDecimal - The pre-discount charge
	 */
	private BigDecimal calculatePreDiscountCharge(Tool tool, int chargeableDays) {
		BigDecimal dailyCharge = new BigDecimal(tool.getToolType().dailyCharge);
		
		return dailyCharge.multiply(new BigDecimal(chargeableDays)).setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Calculate the discount amount by multiply the decimal percentage to the pre-discount charge.
	 * @param checkout - Checkout object containing tool and rental length info
	 * @param preDiscountCharge - BigDecimal representation of the transaction amount before discounts applied
	 * @return BigDecimal - The discount amount
	 */
	private BigDecimal calculateDiscountAmount(Checkout checkout, BigDecimal preDiscountCharge) {
		BigDecimal discountPercent = new BigDecimal(checkout.getDiscount() * 0.01);
		
		return discountPercent.multiply(preDiscountCharge).setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * * Calculate the final charge by subtracting the discount amount from the pre-discount amount
	 * @param preDiscountCharge - BigDecimal representation of the transaction amount before discounts applied
	 * @param discountAmount - BigDecimal representation of how much of a discount the customer gets
	 * @return
	 */
	private BigDecimal calculateFinalCharge(BigDecimal preDiscountCharge, BigDecimal discountAmount) {
		return preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
	}
}
