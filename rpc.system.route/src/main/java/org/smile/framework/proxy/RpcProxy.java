package org.smile.framework.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.smile.framework.core.generator.UUIDGenerator;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.route.RouteRuleMapper;
import org.smile.framework.route.RouteRuleProcessor;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkSession;
import org.smile.framework.route.service.ServiceMetaData;
import org.smile.framework.route.service.ServiceMetaDataFactory;
import org.smile.framework.rpc.data.RpcRequest;
import org.smile.framework.rpc.data.RpcResponse;
import org.smile.framework.util.IdentifierUtil;
import org.smile.framework.util.NetworkUtil;

public class RpcProxy implements InvocationHandler {
	
	private ServiceMetaDataFactory serviceMetaDataFactory;
	
	public RpcProxy(ServiceMetaDataFactory serviceMetaDataFactory) {
		this.serviceMetaDataFactory = serviceMetaDataFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method!=null) {
			if (args!=null && args.length>0) {				
				List parameters = new ArrayList(args.length);
				String identifier = IdentifierUtil.generateIdentifier(method);
				for (Object object:args) {
					parameters.add(object);
				}
				String rule = RouteRuleProcessor.generateRouteRule(method, args);
				ServiceMetaData serviceMetaData = serviceMetaDataFactory.getServiceMetaDataByInterface(method.getDeclaringClass());
				RpcRequest rpcRequest = RouteRuleMapper.routeRpcContext(serviceMetaData, rule, method);
				rpcRequest.setTraceId(UUIDGenerator.generateUUID());
				rpcRequest.setAppName("system");
				rpcRequest.setServiceName(method.getDeclaringClass().getCanonicalName());
				rpcRequest.setMethodName(method.getName());
				rpcRequest.setParameters(parameters);
				rpcRequest.setIdentifier(identifier);

				LogUtil.i(String.format("Send Rpc Request:(%s)", rpcRequest.toString()));

				NetworkRequest networkRequest = new NetworkRequest(rpcRequest);
				NetworkSession networkSession = new NetworkSession(networkRequest);
				
				String serverIp = (rpcRequest.getIsProxied()?rpcRequest.getProxyUrl():rpcRequest.getServerUrl());
				int serverPort = (rpcRequest.getIsProxied()?6666:8888);

				NetworkUtil.sendRequest(serverIp, serverPort, networkSession);

				RpcResponse rpcResponse = (RpcResponse)networkSession.getNetworkResponse().getResponseData();
				if (rpcResponse.isSuccess()) {
					Object response = rpcResponse.getData();
					return response;
				} else {
					Class<?> exceptionClass= Class.forName(rpcResponse.getExceptionClass());
					Constructor<?> constructor = exceptionClass.getConstructor(new Class[]{String.class});
					Exception exception = (Exception)constructor.newInstance(rpcResponse.getMessage());
					throw exception;
				}
			}
		}
		return null;
	}
}
