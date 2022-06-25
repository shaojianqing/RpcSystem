package org.smile.service.accountbill.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.smile.framework.core.util.AssertUtil;
import org.smile.framework.orm.transaction.TransactionCallback;
import org.smile.framework.orm.transaction.TransactionTemplate;
import org.smile.service.accountbill.dao.IAccountBillDao;
import org.smile.service.accountinfo.dao.IAccountInfoDao;
import org.smile.service.domain.accountbill.AccountBill;
import org.smile.service.domain.accountbill.BillTypeEnum;
import org.smile.service.domain.accountinfo.AccountInfo;
import org.smile.service.domain.base.Base;
import org.smile.service.domain.record.OperateRecord;
import org.smile.service.domain.record.OperateTypeEnum;
import org.smile.service.domain.record.TargetTypeEnum;
import org.smile.service.domain.sequence.SequenceEnum;
import org.smile.service.exception.BizException;
import org.smile.service.facade.IAccountBillService;
import org.smile.service.facade.IUserInfoService;
import org.smile.service.operaterecord.dao.IOperateRecordDao;
import org.smile.service.request.AccountBillOperRequest;
import org.smile.service.sequence.ISequenceService;

public class AccountBillServiceImpl implements IAccountBillService {
	
	private IAccountBillDao accountBillDao;
	
	private IAccountInfoDao accountInfoDao;
	
	private IOperateRecordDao operateRecordDao;
	
	private IUserInfoService userInfoService;
	
	private TransactionTemplate  transactionTemplate;
	
	private ISequenceService sequenceService;
	
	public List<AccountBill> queryAccountBillByUserInfoId(String userInfoId) {
		return accountBillDao.queryAccountBillByUserInfoId(userInfoId);
	}
	
	public AccountBill operateAccountBill(final AccountBillOperRequest request) throws Exception {
		if (request!=null) {
			
			AssertUtil.checkNotBlank(request.getUserInfoId(), "用户id不能为空哦!");
			AssertUtil.checkNotNull(request.getAmount(), "账单金额不能为空哦!");
			AssertUtil.checkNotBlank(request.getBillType(), "账单类型不能为空哦!");
			
			AccountBill accountBill = transactionTemplate.execute(new TransactionCallback<AccountBill>(){

				public AccountBill doTransaction() {
					
					String accountBillId = sequenceService.createBizSequence(SequenceEnum.ACCOUNT_BILL);
					Timestamp currentTime = new Timestamp(new Date().getTime());
					
					AccountBill accountBill = new AccountBill();
					accountBill.setId(accountBillId);
					accountBill.setAccountBillNo(accountBillId);
					accountBill.setAmount(request.getAmount());
					accountBill.setBillType(request.getBillType());
					accountBill.setUserInfoId(request.getUserInfoId());
					accountBill.setBillTime(currentTime.getTime());
					accountBill.setIsValid(Base.TRUE);
					accountBill.setCreateTime(currentTime);
					accountBill.setOperateTime(currentTime);

					AccountInfo accountInfo = accountInfoDao.getAccountInfoByUserInfoId(accountBill.getUserInfoId());
					if (BillTypeEnum.EXPANSE.getType().equals(accountBill.getBillType())) {
						if (accountInfo.getTotalBalance()<accountBill.getAmount()) {
							String message = String.format("AccountInfo has no enough balance for expense! userInfoId:%s", accountBill.getUserInfoId());
							throw new BizException(message);
						} else {
							Double totalBalance = accountInfo.getTotalBalance();
							totalBalance -= accountBill.getAmount();
							accountInfo.setTotalBalance(totalBalance);
							
							Double totalExpanse = accountInfo.getTotalExpanse();
							totalExpanse += accountBill.getAmount();
							accountInfo.setTotalExpanse(totalExpanse);
							accountInfo.setOperateTime(currentTime);
						}
					} else if (BillTypeEnum.PROFIT.getType().equals(accountBill.getBillType())) {
						Double totalBalance = accountInfo.getTotalBalance();
						totalBalance += accountBill.getAmount();
						accountInfo.setTotalBalance(totalBalance);
						
						Double totalProfit = accountInfo.getTotalProfit();
						totalProfit += accountBill.getAmount();
						accountInfo.setTotalProfit(totalProfit);
						accountInfo.setOperateTime(currentTime);
					}
					
					accountBill = accountBillDao.saveAccountBill(accountBill);
					accountInfo = accountInfoDao.updateAccountInfo(accountInfo);
					
					OperateRecord operateRecord = new OperateRecord();
					operateRecord.setId(sequenceService.createBizSequence(SequenceEnum.OPERATE_RECORD));
					operateRecord.setOperateType(OperateTypeEnum.SAVE_ACCOUNT_BILL.getType());
					operateRecord.setTargetType(TargetTypeEnum.ACCOUNT_BILL.getTarget());
					operateRecord.setOperatorId(accountBill.getUserInfoId());
					operateRecord.setTargetId(accountBill.getId());
					operateRecord.setIsValid(Base.TRUE);
					operateRecord.setCreateTime(currentTime);
					operateRecord.setOperateTime(currentTime);
					
					operateRecordDao.saveOperateRecord(operateRecord);
					
					operateRecord.setId(sequenceService.createBizSequence(SequenceEnum.OPERATE_RECORD));
					operateRecord.setOperateType(OperateTypeEnum.SAVE_ACCOUNT_INFO.getName());
					operateRecord.setTargetType(TargetTypeEnum.ACCOUNT_INFO.getName());
					operateRecord.setOperatorId(accountInfo.getUserInfoId());
					operateRecord.setTargetId(accountInfo.getId());
					operateRecord.setIsValid(Base.TRUE);
					operateRecord.setCreateTime(currentTime);
					operateRecord.setOperateTime(currentTime);
					
					operateRecordDao.saveOperateRecord(operateRecord);	
					return accountBill;
				}
			});
			return accountBill;
		}
		return null;
	}

	public IAccountBillDao getAccountBillDao() {
		return accountBillDao;
	}

	public void setAccountBillDao(IAccountBillDao accountBillDao) {
		this.accountBillDao = accountBillDao;
	}

	public IAccountInfoDao getAccountInfoDao() {
		return accountInfoDao;
	}

	public void setAccountInfoDao(IAccountInfoDao accountInfoDao) {
		this.accountInfoDao = accountInfoDao;
	}

	public IOperateRecordDao getOperateRecordDao() {
		return operateRecordDao;
	}

	public void setOperateRecordDao(IOperateRecordDao operateRecordDao) {
		this.operateRecordDao = operateRecordDao;
	}

	public IUserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(IUserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public ISequenceService getSequenceService() {
		return sequenceService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
}
