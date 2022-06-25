package org.smile.service.accountinfo.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.smile.framework.orm.template.SqlMapClientTemplate;
import org.smile.service.accountinfo.dao.IAccountInfoDao;
import org.smile.service.domain.accountinfo.AccountInfo;
import org.smile.service.exception.BizException;

public class AccountInfoDaoImpl implements IAccountInfoDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	public AccountInfo getAccountInfoByUserInfoId(String userInfoId) {
		if (StringUtils.isNotBlank(userInfoId)) {
			try {
				return (AccountInfo)sqlMapClientTemplate.queryForObject("org.smile.service.domain.accountinfo.getAccountInfoByUserInfoId", userInfoId);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			throw new BizException("UserInfoId can not be Empty!");
		}
	}
	
	public AccountInfo saveAccountInfo(AccountInfo accountInfo) {
		if (accountInfo!=null) {
			try {
				sqlMapClientTemplate.execute("org.smile.service.domain.accountinfo.saveAccountInfo", accountInfo);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return accountInfo;
	}
	
	public AccountInfo updateAccountInfo(AccountInfo accountInfo) {
		if (accountInfo!=null) {
			try {
				sqlMapClientTemplate.execute("org.smile.service.domain.accountinfo.updateAccountInfo", accountInfo);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return accountInfo;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
