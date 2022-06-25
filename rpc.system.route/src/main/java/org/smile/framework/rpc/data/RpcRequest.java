package org.smile.framework.rpc.data;

import java.io.Serializable;
import java.util.List;

public class RpcRequest<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String serverUrl;
	
	private String proxyUrl;
	
	private boolean isProxied = false;
	
	private String appName;
	
	private String serviceName;
	
	private String methodName;
	
	private String identifier;
	
	private String traceId;
	
	private List<T> parameters;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<T> getParameters() {
		return parameters;
	}

	public void setParameters(List<T> parameters) {
		this.parameters = parameters;
	}

	public String getProxyUrl() {
		return proxyUrl;
	}

	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}

	public boolean getIsProxied() {
		return isProxied;
	}

	public void setIsProxied(boolean isProxied) {
		this.isProxied = isProxied;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	public String toString() {
		String description = String.format("traceId:%s,appName:%s,serverUrl:%s,isProxied:%s,proxyUrl:%s,identifier:%s", 
				traceId, appName, serverUrl, String.valueOf(isProxied), proxyUrl, identifier);
		return description;
	}
}