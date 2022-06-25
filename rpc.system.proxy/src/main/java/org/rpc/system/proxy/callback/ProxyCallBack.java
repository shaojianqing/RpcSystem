package org.rpc.system.proxy.callback;

import org.smile.framework.rpc.data.RpcRequest;

public interface ProxyCallBack {
	
	public RpcRequest onProxy(byte[] requestData) throws Exception;
	
	public void onResult(String result);
}
