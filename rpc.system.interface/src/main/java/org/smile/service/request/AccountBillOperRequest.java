package org.smile.service.request;

import java.io.Serializable;

public class AccountBillOperRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String userInfoId;
	
	private Double amount;
	
	private String billType;

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
}
