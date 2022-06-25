package org.smile.service.facade;

import java.util.List;

import org.smile.framework.route.RouteRule;
import org.smile.service.domain.accountbill.AccountBill;
import org.smile.service.request.AccountBillOperRequest;

public interface IAccountBillService {
	
	@RouteRule(ruleGenerator="org.smile.service.generator.UserInfoIdGenerator")
	public List<AccountBill> queryAccountBillByUserInfoId(String userInfoId);
	
	@RouteRule(ruleGenerator="org.smile.service.generator.AccountBillUidGenerator")
	public AccountBill operateAccountBill(final AccountBillOperRequest request) throws Exception;
}
