package org.smile.service.accountinfo.dao;

import org.smile.service.domain.accountinfo.AccountInfo;

public interface IAccountInfoDao {
	
	public AccountInfo getAccountInfoByUserInfoId(String userInfoId);
	
	public AccountInfo saveAccountInfo(AccountInfo accountInfo);
	
	public AccountInfo updateAccountInfo(AccountInfo accountInfo);
}
