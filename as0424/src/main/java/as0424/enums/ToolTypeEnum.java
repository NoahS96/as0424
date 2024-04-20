package main.java.as0424.enums;

public enum ToolTypeEnum {

	CHAINSAW("Chainsaw", 1.99, true, false, true),
	LADDER("Ladder", 1.49, true, true, false),
	JACKHAMMER("Jackhammer", 2.99, true, false, false);
	
	public final String name;
	public final double dailyCharge;
	public final boolean hasWeekdayCharge;
	public final boolean hasWeekendCharge;
	public final boolean hasHolidayCharge;
	
	private ToolTypeEnum(String toolTypeName, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
		this.name = toolTypeName;
		this.dailyCharge = dailyCharge;
		this.hasWeekdayCharge = weekdayCharge;
		this.hasWeekendCharge = weekendCharge;
		this.hasHolidayCharge = holidayCharge;
	}
}
