package org.smile.service.domain.accountinfo;

import org.smile.service.domain.base.Base;

public class AccountInfo extends Base {
	
	private String userInfoId;
	
	private Double totalBalance;
	
	private Double totalExpanse;
	
	private Double totalProfit;

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Double getTotalExpanse() {
		return totalExpanse;
	}

	public void setTotalExpanse(Double totalExpanse) {
		this.totalExpanse = totalExpanse;
	}

	public Double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}
}
