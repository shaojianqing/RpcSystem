package org.smile.service.domain.accountbill;

import org.smile.service.domain.base.Base;

public class AccountBill extends Base {
	
	private String userInfoId;
	
	private String accountBillNo;
	
	private Double amount;
	
	private String billType;
	
	private Long billTime;

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public String getAccountBillNo() {
		return accountBillNo;
	}

	public void setAccountBillNo(String accountBillNo) {
		this.accountBillNo = accountBillNo;
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

	public Long getBillTime() {
		return billTime;
	}

	public void setBillTime(Long billTime) {
		this.billTime = billTime;
	}
}
