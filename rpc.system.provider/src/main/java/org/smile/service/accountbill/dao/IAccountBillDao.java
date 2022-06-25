package org.smile.service.accountbill.dao;

import java.util.List;

import org.smile.service.domain.accountbill.AccountBill;

public interface IAccountBillDao {
	
	public List<AccountBill> queryAccountBillByUserInfoId(String userInfoId);
	
	public AccountBill saveAccountBill(AccountBill accountBill);

}
