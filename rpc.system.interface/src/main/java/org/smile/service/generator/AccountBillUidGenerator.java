package org.smile.service.generator;

import org.smile.framework.route.IRuleGenerator;
import org.smile.service.request.AccountBillOperRequest;

public class AccountBillUidGenerator implements IRuleGenerator {

	public String generateRule(Object parameter) {
		if (parameter!=null) {
			if (parameter instanceof AccountBillOperRequest) {
				AccountBillOperRequest request = (AccountBillOperRequest)parameter;
				String userInfoId = request.getUserInfoId();
				if (userInfoId!=null && userInfoId.length()==29) {
					return userInfoId.substring(27, 29);
				}
			}
		}
		return "";
	}
}
