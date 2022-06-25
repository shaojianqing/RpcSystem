package org.smile.service.userinfo.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.smile.framework.core.util.AssertUtil;
import org.smile.framework.orm.transaction.TransactionCallback;
import org.smile.framework.orm.transaction.TransactionTemplate;
import org.smile.service.accountinfo.dao.IAccountInfoDao;
import org.smile.service.domain.accountinfo.AccountInfo;
import org.smile.service.domain.base.Base;
import org.smile.service.domain.record.OperateRecord;
import org.smile.service.domain.record.OperateTypeEnum;
import org.smile.service.domain.record.TargetTypeEnum;
import org.smile.service.domain.sequence.SequenceEnum;
import org.smile.service.domain.userinfo.UserInfo;
import org.smile.service.facade.IUserInfoService;
import org.smile.service.operaterecord.dao.IOperateRecordDao;
import org.smile.service.sequence.ISequenceService;
import org.smile.service.userinfo.dao.IUserInfoDao;

public class UserInfoServiceImpl implements IUserInfoService {
	
	private IUserInfoDao userInfoDao;
	
	private IAccountInfoDao accountInfoDao;
	
	private IOperateRecordDao operateRecordDao;
	
	private TransactionTemplate  transactionTemplate;
	
	private ISequenceService sequenceService;

	public UserInfo queryUserInfoByUserInfoId(String userInfoId) {
		
		return userInfoDao.getUserInfoByUserInfoId(userInfoId);
	}
	
	public UserInfo saveUserInfo(UserInfo userInfo) {
		
		return userInfoDao.saveUserInfo(userInfo);
	}
	
	public UserInfo updateUserInfo(UserInfo userInfo) {
		return userInfoDao.updateUserInfo(userInfo);
	}
	
	public UserInfo registeUserInfo(final String username, final String password, final String name, final Date birthday, final String idNumber) throws Exception {
		
		AssertUtil.checkNotBlank(username, "用户名不能为空哦！");
		AssertUtil.checkNotBlank(password, "用户密码不能为空哦！");
		AssertUtil.checkNotBlank(name, "用户姓名不能为空哦！");
		AssertUtil.checkNotNull(birthday, "用户出生日期不能为空哦！");
		AssertUtil.checkNotBlank(idNumber, "用户身份证号不能为空哦！");
		
		UserInfo userInfo = (UserInfo)transactionTemplate.execute(new TransactionCallback<UserInfo>() {

			public UserInfo doTransaction() {
				
				Timestamp currentTime = new Timestamp(new Date().getTime());
				
				UserInfo userInfo = new UserInfo();
				userInfo.setId(sequenceService.createBizSequence(SequenceEnum.USER_INFO));
				userInfo.setUsername(username);
				userInfo.setPassword(password);
				userInfo.setName(name);
				userInfo.setBirthday(birthday);
				userInfo.setIdNumber(idNumber);
				userInfo.setIsValid(Base.TRUE);
				userInfo.setCreateTime(currentTime);
				userInfo.setOperateTime(currentTime);
				
				userInfoDao.saveUserInfo(userInfo); 
				
				AccountInfo accountInfo = new AccountInfo();
				accountInfo.setId(sequenceService.createBizSequence(SequenceEnum.ACCOUNT_INFO));
				accountInfo.setUserInfoId(userInfo.getId());
				accountInfo.setTotalBalance(0.0d);
				accountInfo.setTotalExpanse(0.0d);
				accountInfo.setTotalProfit(0.0d);
				accountInfo.setIsValid(Base.TRUE);
				accountInfo.setCreateTime(currentTime);
				accountInfo.setOperateTime(currentTime);
				
				accountInfoDao.saveAccountInfo(accountInfo);
				
				OperateRecord operateRecord = new OperateRecord();
				operateRecord.setId(sequenceService.createBizSequence(SequenceEnum.OPERATE_RECORD));
				operateRecord.setOperateType(OperateTypeEnum.SAVE_USER_INFO.getType());
				operateRecord.setTargetType(TargetTypeEnum.USER_INFO.getTarget());
				operateRecord.setOperatorId(userInfo.getId());
				operateRecord.setTargetId(userInfo.getId());
				operateRecord.setIsValid(Base.TRUE);
				operateRecord.setCreateTime(currentTime);
				operateRecord.setOperateTime(currentTime);
				
				operateRecordDao.saveOperateRecord(operateRecord);
				
				operateRecord.setId(sequenceService.createBizSequence(SequenceEnum.OPERATE_RECORD));
				operateRecord.setOperateType(OperateTypeEnum.SAVE_ACCOUNT_INFO.getType());
				operateRecord.setTargetType(TargetTypeEnum.ACCOUNT_INFO.getTarget());
				operateRecord.setOperatorId(userInfo.getId());
				operateRecord.setTargetId(accountInfo.getId());
				operateRecord.setIsValid(Base.TRUE);
				operateRecord.setCreateTime(currentTime);
				operateRecord.setOperateTime(currentTime);
				
				operateRecordDao.saveOperateRecord(operateRecord);
				
				return userInfo;
			}
		});
		return userInfo;
	}

	public IUserInfoDao getUserInfoDao() {
		return userInfoDao;
	}

	public void setUserInfoDao(IUserInfoDao userInfoDao) {
		this.userInfoDao = userInfoDao;
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
