package org.smile.service.provider;

import org.smile.framework.core.context.ApplicationContext;
import org.smile.framework.core.context.impl.ClassPathXmlApplicationContext;
import org.smile.service.domain.userinfo.UserInfo;
import org.smile.service.facade.IUserInfoService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserInfoServiceTest {
  @Test
  public void testUserInfo() throws Exception {
	  
	  	String configfilename = "src/main/resources/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(configfilename);
		
		IUserInfoService userInfoService = (IUserInfoService)context.getBean("userInfoService");
		
		UserInfo userInfo = userInfoService.queryUserInfoByUserInfoId("20862689786");
		
		Assert.assertNotNull(userInfo);
		Assert.assertEquals(userInfo.getIdNumber(), "330183198902082113");
		Assert.assertEquals(userInfo.getUsername(), "shaojianqing");
		Assert.assertEquals(userInfo.getId(), "20862689786");
		Assert.assertEquals(userInfo.getPassword(), "12345678");
  }
}
