package org.smile.service.domain.record;

public enum TargetTypeEnum {
	
	USER_INFO("UserInfo", "UserInfo"),
	
	ACCOUNT_BILL("AccountBill", "AccountBill"),
	
	ACCOUNT_INFO("AccountInfo", "AccountInfo");
	
	private final String target;
	
	private final String name;
	
	private TargetTypeEnum(String target, String name) {
		this.target = target;
		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public String getName() {
		return name;
	}
}