package org.smile.service.provider;

import org.smile.framework.core.context.ApplicationContext;
import org.smile.framework.core.context.impl.ClassPathXmlApplicationContext;
import org.smile.framework.rpc.RpcServiceContext;

public class ServerInstance {
	
	public static void main(String[] args) throws Exception {
		
		String confregUrl = "confreg.system.org";
		String configfilename = "src/main/resources/applicationContext.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(configfilename);
		RpcServiceContext rpcServiceContext = new RpcServiceContext(context, configfilename);
		
		rpcServiceContext.initConfregConfig(confregUrl);
		rpcServiceContext.startService();
	}
}