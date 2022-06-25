package org.smile.service.consumer.accountinfo.action;

import org.smile.framework.route.annotation.RpcReference;
import org.smile.service.domain.accountinfo.AccountInfo;
import org.smile.service.facade.IAccountInfoService;

public class AccountInfoAction {
	
	@RpcReference(referenceId="accountInfoService")
	private IAccountInfoService accountInfoService;
	
	public void executeQueryAccountInfo(String userInfoId) {
		AccountInfo accountInfo = accountInfoService.queryAccountInfoByUserInfoId(userInfoId);
		if (accountInfo!=null) {
			System.out.println();
			System.out.println("=================AccountInfo:================");
			
			System.out.println("id:					" + accountInfo.getId());
			System.out.println("userInfoId:				" + accountInfo.getUserInfoId());
			System.out.println("totalBalance:				" + accountInfo.getTotalBalance());
			System.out.println("totalExpanse:				" + accountInfo.getTotalExpanse());
			System.out.println("totalProfit:				" + accountInfo.getTotalProfit());
		}
	}

	public IAccountInfoService getAccountInfoService() {
		return accountInfoService;
	}

	public void setAccountInfoService(IAccountInfoService accountInfoService) {
		this.accountInfoService = accountInfoService;
	}
}
