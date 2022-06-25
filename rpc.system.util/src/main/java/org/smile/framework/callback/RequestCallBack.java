package org.smile.framework.callback;

import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkResponse;

public interface RequestCallBack {
	
	public NetworkResponse onService(NetworkRequest networkRequest) throws Exception;
}
