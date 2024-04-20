package main.java.as0424.pojo.tool;

import main.java.as0424.enums.ToolTypeEnum;

/**
 * Abstract Tool class that all tools [Chainsaw, Ladder, Jackhammer, etc.] should share with common methods.
 * Any tools which require additional fields or methods can be implemented in their own extended class.
 * 
 */
public abstract class Tool {
	
	private String toolCode = "";
	private ToolTypeEnum toolType;
	private String brand = "";
	
	/**
	 * @param toolCode - Code used to identify specific tool
	 * @param toolType - Type of tool
	 * @param brand - Brand name
	 */
	public Tool(String toolCode, ToolTypeEnum toolType, String brand) {
		this.setToolCode(toolCode);
		this.setToolType(toolType);
		this.setBrand(brand);
	}

	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}
	
	public ToolTypeEnum getToolType() {
		return toolType;
	}

	public void setToolType(ToolTypeEnum toolType) {
		this.toolType = toolType;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
}
