package org.smile.service.domain.record;

public enum OperateTypeEnum {
	
	SAVE_USER_INFO("saveUserInfo", "saveUserInfo"),
	
	UPDATE_USER_INFO("updateUserInfo", "updateUserInfo"),
	
	DELETE_USER_INFO("deleteUserInfo", "deleteUserInfo"),
	
	SAVE_ACCOUNT_BILL("saveAccountBill", "saveAccountBill"),
	
	UPDATE_ACCOUNT_BILL("updateAccountBill", "updateAccountBill"),
	
	DELETE_ACCOUNT_BILL("deleteAccountBill", "deleteAccountBill"),
	
	SAVE_ACCOUNT_INFO("saveAccountInfo", "saveAccountInfo"),
	
	UPDATE_ACCOUNT_INFO("updateAccountInfo", "updateAccountInfo"),
	
	DELETE_ACCOUNT_INFO("deleteAccountInfo", "deleteAccountInfo");
	
	private final String type;
	
	private final String name;
	
	private OperateTypeEnum(String type, String name) {
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