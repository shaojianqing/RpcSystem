package org.smile.service.accountbill.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.smile.framework.orm.template.SqlMapClientTemplate;
import org.smile.service.accountbill.dao.IAccountBillDao;
import org.smile.service.domain.accountbill.AccountBill;

public class AccountBillDaoImpl implements IAccountBillDao {
	
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@SuppressWarnings("unchecked")
	public List<AccountBill> queryAccountBillByUserInfoId(String userInfoId) {
		if (StringUtils.isNotBlank(userInfoId)) {
			try {
				return (List<AccountBill>)sqlMapClientTemplate.queryForList("org.smile.service.domain.accountbill.queryAccountBillByUserId", userInfoId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ArrayList<AccountBill>();
	}
	
	public AccountBill saveAccountBill(AccountBill accountBill) {
		if (accountBill!=null) {
			try {
				sqlMapClientTemplate.execute("org.smile.service.domain.accountbill.saveAccountBill", accountBill);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return accountBill;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
