package org.rpc.system.test;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Date;

import org.smile.service.domain.base.Base;
import org.smile.service.domain.userinfo.UserInfo;


public class ApplicationTest 
{
    public static void main( String[] args ) {
    	
    	UserInfo userInfo = new UserInfo();
    	userInfo.setBirthday(new Date());
    	userInfo.setId("20170607234629802000000000001");
    	userInfo.setIdNumber("330183198902082113");
    	userInfo.setIsValid(Base.TRUE);
    	userInfo.setName("邵建青");
    	userInfo.setUsername("shaojianqing");
    	userInfo.setPassword("12345678");
    	Timestamp current = new Timestamp(new Date().getTime());
    	userInfo.setCreateTime(current);
    	userInfo.setOperateTime(current);

    	System.out.println(userInfo);
    	
    }
}