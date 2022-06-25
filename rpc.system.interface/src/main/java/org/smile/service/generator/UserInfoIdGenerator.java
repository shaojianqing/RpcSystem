package org.smile.service.generator;

import org.smile.framework.route.IRuleGenerator;
import org.smile.service.domain.userinfo.UserInfo;

public class UserInfoIdGenerator implements IRuleGenerator {

	public String generateRule(Object parameter) {
		if (parameter!=null) {
			if (parameter instanceof String) {
				String userInfoId = (String)parameter;
				if (userInfoId.length()==29) {
					return userInfoId.substring(27, 29);
				}
			} else if (parameter instanceof UserInfo) {
				UserInfo userInfo = (UserInfo)parameter;
				String userInfoId = userInfo.getId();
				if (userInfoId.length()==29) {
					return userInfoId.substring(27, 28);
				}
			}
		}
		return "";
	}
}
