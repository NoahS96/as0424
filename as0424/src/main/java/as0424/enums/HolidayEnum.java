package main.java.as0424.enums;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * This is an Enum list of holidays which allows for the addition of more holidays if needed
 * and includes two helper methods which may be shared if future holidays require them.
 * The reference day is either the exact day and month they are consistent for the holiday OR
 * the month is set to whatever month the holiday occurs in and the day is set to 1 in which case
 * the day is not used as it doesn't matter.
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
			return HolidayEnum.getFirstMonday(this.referenceMonth, referenceYear);
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

	/**
	 * Grabs the closest weekday to the holiday if the holiday falls on a weekend.
	 * Otherwise, it returns the date provided as it is a weekday.
	 * 
	 * @param referenceDay
	 * @param referenceMonth
	 * @param referenceYear
	 * @return
	 */
	private static LocalDate getClosestWeekday(int referenceDay, int referenceMonth, int referenceYear) {
		LocalDate referenceDate = LocalDate.of(referenceYear, referenceMonth, referenceDay);
		DayOfWeek dayOfWeek = referenceDate.getDayOfWeek();
		
		if (dayOfWeek.equals(DayOfWeek.SATURDAY)) { 
			return referenceDate.minusDays(1); 
		} else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
			return referenceDate.plusDays(1);
		} else {
			return referenceDate;
		}
	}
	
	/**
	 * Grabs the first Monday of the reference month using the built in
	 * Java time TemporalAdjusters.
	 * 
	 * @param referenceDay
	 * @param referenceMonth
	 * @param referenceYear
	 * @return
	 */
	private static LocalDate getFirstMonday(int referenceMonth, int referenceYear) {
		LocalDate referenceDate = LocalDate.of(referenceYear, referenceMonth, 1);
		return referenceDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
	}
}
