package org.smile.service.userinfo.dao.impl;

import org.smile.framework.orm.template.SqlMapClientTemplate;
import org.smile.service.domain.userinfo.UserInfo;
import org.smile.service.userinfo.dao.IUserInfoDao;

public class UserInfoDaoImpl implements IUserInfoDao {
	
	private SqlMapClientTemplate sqlMapClientTemplate;

	public UserInfo getUserInfoByUserInfoId(String userInfoId) {
		try {
			UserInfo userInfo = (UserInfo)sqlMapClientTemplate.
					queryForObject("org.smile.service.domain.userinfo.getUserInfoById", userInfoId);
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public UserInfo saveUserInfo(UserInfo userInfo) {
		if (userInfo!=null) {
			try {
				int result = sqlMapClientTemplate.execute("org.smile.service.domain.userinfo.saveUserInfo", userInfo);
				if (result>0) {
					return userInfo;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public UserInfo updateUserInfo(UserInfo userInfo) {
		if (userInfo!=null) {
			try {
				int result = sqlMapClientTemplate.execute("org.smile.service.domain.userinfo.updateUserInfo", userInfo);
				if (result>0) {
					return userInfo;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
