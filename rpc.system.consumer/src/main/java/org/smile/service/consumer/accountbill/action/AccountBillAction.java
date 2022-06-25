package org.smile.service.consumer.accountbill.action;

import java.util.List;

import org.smile.framework.route.annotation.RpcReference;
import org.smile.service.domain.accountbill.AccountBill;
import org.smile.service.domain.accountbill.BillTypeEnum;
import org.smile.service.facade.IAccountBillService;
import org.smile.service.request.AccountBillOperRequest;

public class AccountBillAction {
	
	@RpcReference(referenceId="accountBillService")
	private IAccountBillService accountBillService;
	
	public void executeQueryAccountBill(String userInfoId) {
		
		List<AccountBill> accountBillList = accountBillService.queryAccountBillByUserInfoId(userInfoId);

		System.out.println();
		System.out.println("==============AccountBill List:==============");
		
		if (accountBillList!=null && accountBillList.size()>0) {
			for (AccountBill accountBill:accountBillList) {
				System.out.println("id:					" + accountBill.getId());
				System.out.println("accountBillNo:				" + accountBill.getAccountBillNo());
				System.out.println("billType:				" + accountBill.getBillType());
				System.out.println("amount:					" + accountBill.getAmount());
				System.out.println("billTime:				" + accountBill.getBillTime());
				System.out.println();
			    System.out.println("---------------------------------------------");
			}
		}
	}
	
	public void executeOperateAccountBill(String userInfoId) throws Exception {
		
		AccountBillOperRequest operRequest = new AccountBillOperRequest();
		operRequest.setAmount(128.00);
		operRequest.setBillType(BillTypeEnum.PROFIT.getType());
		operRequest.setUserInfoId(userInfoId);
		AccountBill accountBill = accountBillService.operateAccountBill(operRequest);
		
		System.out.println();
		System.out.println("===========Operate AccountBill:==============");
		
		System.out.println("id:					" + accountBill.getId());
		System.out.println("accountBillNo:				" + accountBill.getAccountBillNo());
		System.out.println("billType:				" + accountBill.getBillType());
		System.out.println("amount:					" + accountBill.getAmount());
		System.out.println("billTime:				" + accountBill.getBillTime());
	}

	public IAccountBillService getAccountBillService() {
		return accountBillService;
	}

	public void setAccountBillService(IAccountBillService accountBillService) {
		this.accountBillService = accountBillService;
	}
}