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
	JULY_FOURTH(LocalDate.of(Year.now().getValue(), 7, 4)) {
		@Override
		public LocalDate getObservableDate() {
			return HolidayEnum.getClosestWeekday(this.referenceDate);
		}
	},
	LABOR_DAY(LocalDate.of(Year.now().getValue(), 9, 1)) {
		@Override
		public LocalDate getObservableDate() {
			return HolidayEnum.getFirstMonday(this.referenceDate);
		}
	},
	TEST_DAY(LocalDate.of(Year.now().getValue(), 4, 20)) {
		@Override
		public LocalDate getObservableDate() {
			return HolidayEnum.getClosestWeekday(this.referenceDate);
		}
	},;
	
	protected LocalDate referenceDate;
	public abstract LocalDate getObservableDate();
	
	
	private HolidayEnum(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	private static LocalDate getClosestWeekday(LocalDate referenceDate) {
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
	
	private static LocalDate getFirstMonday(LocalDate referenceDate) {
		// Get a local date representation of September of this year and use the built in
		// Java TemporalAdjusters to get the first Monday of the month.
		return referenceDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
	}
}
