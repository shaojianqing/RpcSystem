package org.smile.service.accountinfo.impl;

import org.smile.service.accountinfo.dao.IAccountInfoDao;
import org.smile.service.domain.accountinfo.AccountInfo;
import org.smile.service.facade.IAccountInfoService;

public class AccountInfoServiceImpl implements IAccountInfoService {
	
	private IAccountInfoDao accountInfoDao;

	public AccountInfo queryAccountInfoByUserInfoId(String userInfoId) {
		return accountInfoDao.getAccountInfoByUserInfoId(userInfoId);	
	}

	public IAccountInfoDao getAccountInfoDao() {
		return accountInfoDao;
	}

	public void setAccountInfoDao(IAccountInfoDao accountInfoDao) {
		this.accountInfoDao = accountInfoDao;
	}
}