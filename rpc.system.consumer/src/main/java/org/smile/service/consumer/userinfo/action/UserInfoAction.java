package org.smile.service.consumer.userinfo.action;

import org.smile.framework.route.annotation.RpcReference;
import org.smile.service.domain.userinfo.UserInfo;
import org.smile.service.facade.IUserInfoService;

public class UserInfoAction {
	
	@RpcReference(referenceId="userInfoService")
	private IUserInfoService userInfoService;

	public void executeQueryUserInfo(String userInfoId) {
		UserInfo userInfo = userInfoService.queryUserInfoByUserInfoId(userInfoId);
		if (userInfo!=null) {

			System.out.println();
			System.out.println("==================UserInfo:==================");
			
			System.out.println("id:					" + userInfo.getId());
			System.out.println("idNumber:				" + userInfo.getIdNumber());
			System.out.println("name:					" + userInfo.getName());
			System.out.println("password:				" + userInfo.getPassword());
			System.out.println("username:				" + userInfo.getUsername());
			System.out.println("birthday:				" + userInfo.getBirthday());
		}
	}
	
	public IUserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(IUserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
}
