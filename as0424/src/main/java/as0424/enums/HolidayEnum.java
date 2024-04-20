package main.java.as0424.enums;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

/**
 * This is a Enum list of holidays which allows for the addition of more holidays if needed
 * and includes two helper methods which may be shared if future holidays require them.
 */
public enum HolidayEnum {
	JULY_FOURTH(7, 4) {
		@Override
		public LocalDate getObservableDate(int referenceYear) { 
			return HolidayEnum.getClosestWeekday(this.referenceDay, this.referenceMonth, referenceYear);
		}
	},
	LABOR_DAY(9, 1) {
		@Override
		public LocalDate getObservableDate(int referenceYear) {
			return HolidayEnum.getFirstMonday(this.referenceDay, this.referenceMonth, referenceYear);
		}
	};
	
	protected int referenceDay;
	protected int referenceMonth;
	
	/**
	 * Looks up the observable date of a holiday based on the year provided.
	 * @param referenceYear - The desired year of the observable holiday
	 * @return LocalDate - The observable holiday date
	 */
	public abstract LocalDate getObservableDate(int referenceYear);
	
	
	private HolidayEnum(int referenceMonth, int referenceDay) {
		this.referenceMonth = referenceMonth;
		this.referenceDay = referenceDay;
	}

	private static LocalDate getClosestWeekday(int referenceDay, int referenceMonth, int referenceYear) {
		LocalDate referenceDate = LocalDate.of(referenceYear, referenceMonth, referenceDay);
		DayOfWeek dayOfWeek = referenceDate.getDayOfWeek();
		
		// Check if July 4th falls on a weekend and return the closest day as observed date.
		// Otherwise, return our original date.
		if (dayOfWeek.equals(DayOfWeek.SATURDAY)) { 
			return referenceDate.minusDays(1); 
		} else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
			return referenceDate.plusDays(1);
		} else {
			return referenceDate;
		}
	}
	
	private static LocalDate getFirstMonday(int referenceDay, int referenceMonth, int referenceYear) {
		// Get a local date representation of September of this year and use the built in
		// Java TemporalAdjusters to get the first Monday of the month.
		LocalDate referenceDate = LocalDate.of(referenceYear, referenceMonth, referenceDay);
		return referenceDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
	}
}
