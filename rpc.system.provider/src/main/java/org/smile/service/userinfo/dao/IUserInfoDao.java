package org.smile.service.userinfo.dao;

import org.smile.service.domain.userinfo.UserInfo;

public interface IUserInfoDao {
	
	public UserInfo getUserInfoByUserInfoId(String userInfoId);
	
	public UserInfo saveUserInfo(UserInfo userInfo);
	
	public UserInfo updateUserInfo(UserInfo userInfo);
}
