package org.smile.service.facade;

import org.smile.framework.route.RouteRule;
import org.smile.service.domain.accountinfo.AccountInfo;

public interface IAccountInfoService {

	@RouteRule(ruleGenerator="org.smile.service.generator.UserInfoIdGenerator")
	public AccountInfo queryAccountInfoByUserInfoId(String userInfoId);
}
