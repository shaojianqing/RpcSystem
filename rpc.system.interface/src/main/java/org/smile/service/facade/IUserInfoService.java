package org.smile.service.facade;

import java.util.Date;

import org.smile.framework.route.RouteRule;
import org.smile.service.domain.userinfo.UserInfo;

public interface IUserInfoService {
	
	@RouteRule(ruleGenerator="org.smile.service.generator.UserInfoIdGenerator")
	public UserInfo queryUserInfoByUserInfoId(String userInfoId);
	
	@RouteRule(ruleGenerator="org.smile.service.generator.UserInfoIdGenerator")
	public UserInfo saveUserInfo(UserInfo userInfo);
	
	@RouteRule(ruleGenerator="org.smile.service.generator.UserInfoIdGenerator")
	public UserInfo updateUserInfo(UserInfo userInfo);
	
	public UserInfo registeUserInfo(String username, String password, String name, Date birthday, String idNumber) throws Exception;
}
