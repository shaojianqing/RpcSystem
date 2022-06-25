package org.smile.service.consumer;

import org.smile.framework.core.context.ApplicationContext;
import org.smile.framework.core.context.impl.ClassPathXmlApplicationContext;
import org.smile.framework.rpc.RpcReferenceContext;
import org.smile.service.consumer.accountbill.action.AccountBillAction;
import org.smile.service.consumer.accountinfo.action.AccountInfoAction;
import org.smile.service.consumer.userinfo.action.UserInfoAction;

public class ClientInstance {
	
	/*******Local Zone Info*******
	 * 
	 * 	ZoneName:RZ02
	 * 
	 * 	CityName:sh
	 * 
	 * 	IDCName:ETA
	 * 
	 * 	ZoneRange:20-39
	 * 
	 * 	RpcUrl:rz02.system.org
	 * 
	 */
	
	public static void main(String[] args) throws Exception {
		
		String vipUrl = "vip.system.org";
		String proxyUrl = "proxy.system.org";
		String managerUrl = "manager.system.org";
		String confregUrl = "confreg.system.org";
		String configFilename = "zoneInfo.config";
		String contextFilename = "applicationContext.xml";
		
		ApplicationContext context = new ClassPathXmlApplicationContext(contextFilename);
		RpcReferenceContext rpcReferenceContext = new RpcReferenceContext(context, contextFilename);
		rpcReferenceContext.initRpcReference(managerUrl, confregUrl, proxyUrl, vipUrl, configFilename);
		
		/**  userInfoId位于RZ01,userInfoId区段为00-19
		 * 
		 * 	 场景:跨城调用场景  
		 ***/
		//String userInfoId = "20170617154832841000000000008";
		
		/**  userInfoId位于RZ02,userInfoId区段为20-39
		 * 
		 * 	 场景:同IDC同Zone调用场景  
		 ***/
		//String userInfoId = "20170617154833234000000000036";
		
		/**  userInfoId位于RZ03,userInfoId区段为40-59
		 * 
		 * 	 场景:同IDC跨Zone调用场景  
		 ***/
		//String userInfoId = "20170617154833422000000000048";
		
		/**  userInfoId位于RZ04,userInfoId区段为60-79
		 * 
		 * 	 场景:同城跨IDC调用场景   
		 ***/
		String userInfoId = "20170617154833640000000000066";
		
		/**  userInfoId位于RZ05,userInfoId区段为80-99
		 * 
		 *   场景:跨城调用场景
		 ***/
		//String userInfoId = "20170617154833869000000000088";
		
		UserInfoAction userInfoAction = (UserInfoAction)context.getBean("userInfoAction");
		userInfoAction.executeQueryUserInfo(userInfoId);
		
		AccountBillAction accountBillAction = (AccountBillAction)context.getBean("accountBillAction");
		accountBillAction.executeOperateAccountBill(userInfoId);
		accountBillAction.executeQueryAccountBill(userInfoId);
		
		AccountInfoAction accountInfoAction = (AccountInfoAction)context.getBean("accountInfoAction");
		accountInfoAction.executeQueryAccountInfo(userInfoId);
	}
}
