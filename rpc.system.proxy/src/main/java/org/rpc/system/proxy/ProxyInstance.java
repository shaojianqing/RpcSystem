package org.rpc.system.proxy;

import org.rpc.system.proxy.callback.ProxyCallBack;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.rpc.data.RpcRequest;
import org.smile.framework.util.ProtocolUtil;

public class ProxyInstance {

	public static void main(String[] args) {
		
		LogUtil.i("Request Proxy Started^+^");
		ProxyUtil.proxy(6666, new ProxyCallBack(){

			@SuppressWarnings("rawtypes")
			public RpcRequest onProxy(byte[] requestData) throws Exception {
				
				RpcRequest rpcContext = (RpcRequest)ProtocolUtil.transToObject(requestData);
				
				LogUtil.i(String.format("Received Rpc Proxy Request:(%s)", rpcContext.toString()));
				
				LogUtil.i("Received Proxy Request:"+requestData);
				LogUtil.i("The Proxy Info");
				LogUtil.i("The ServerUrl is:" + rpcContext.getServerUrl());
				LogUtil.i("The AppName is:" + rpcContext.getAppName());
				LogUtil.i("The MethodName is:" + rpcContext.getMethodName());
				LogUtil.i("The Identifier is:" + rpcContext.getIdentifier());
				LogUtil.i("The ServiceName is:" + rpcContext.getServiceName());
				
				return rpcContext;
			}

			public void onResult(String result) {
				LogUtil.i("Received Proxy Response:" + result);
			}			
		});		
	}
}