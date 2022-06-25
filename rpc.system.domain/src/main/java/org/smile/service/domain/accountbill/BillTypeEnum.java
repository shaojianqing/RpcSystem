package org.smile.service.domain.accountbill;

public enum BillTypeEnum {
	
	PROFIT("profit", "profit"),
	
	EXPANSE("expanse", "expanse");
	
	private final String type;
	
	private final String name;

	private BillTypeEnum(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
