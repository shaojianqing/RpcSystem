package org.smile.framework.rpc;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.smile.framework.callback.RequestCallBack;
import org.smile.framework.core.context.ApplicationContext;
import org.smile.framework.core.context.impl.ClassPathXmlApplicationContext;
import org.smile.framework.core.exception.BeanNotFoundException;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.route.client.ConfregClient;
import org.smile.framework.rpc.data.RpcResponse;
import org.smile.framework.rpc.data.RpcRequest;
import org.smile.framework.util.IdentifierUtil;
import org.smile.framework.util.ServerUtil;

public class RpcServiceContext {
		
	private ApplicationContext context;
	
	private InputStream configInputStream;
	
	private List<Element> serviceList;
	
	private Document document;
	
	private List<Class<?>> interfaceList = new ArrayList<Class<?>>();
	
	private Map<String, String> namespaceMap = new HashMap<String, String>();
	
	private Map<String, Object> serviceMap = new HashMap<String, Object>();
	
	private Map<String, Method> methodMap = new HashMap<String, Method>();

	public RpcServiceContext(ApplicationContext context, String configurationFileName) throws Exception {
		this.context = context;
		this.configInputStream = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configurationFileName);
		
		this.initRpcServiceContext();
	}

	private void initRpcServiceContext() throws Exception {
		SAXReader xmlReader = new SAXReader();
		
		namespaceMap.put("rpc","http://rpc.system.org/service");
		
		document = xmlReader.read(configInputStream);
        
        initServiceContext(document);
	}

	@SuppressWarnings("unchecked")
	private void initServiceContext(Document document) throws Exception {
		XPath xPath = document.createXPath("//rpc:service");
        xPath.setNamespaceURIs(namespaceMap);
        
		serviceList = xPath.selectNodes(document);
        if (serviceList!=null && serviceList.size()>0) {
        	for (Element serviceNode:serviceList) {
        		initServiceMap(serviceNode);
        	}
        }
	}

	private void initServiceMap(Element serviceNode) throws Exception {
		String reference = serviceNode.attributeValue("reference");
		String interfaze = serviceNode.attributeValue("interface");
		
		Object service = context.getBean(reference);
		if (service!=null) {
			serviceMap.put(interfaze, service);
		} else {
			String message = String.format("Can not find bean:%s, interface:%s", reference, interfaze);
			throw new BeanNotFoundException(message);
		}
		
		Class<?> clazz = Class.forName(interfaze);
		Method methods[] = clazz.getMethods();
		interfaceList.add(clazz);
		for (Method method:methods) {
			methodMap.put(IdentifierUtil.generateIdentifier(method), method);
		}
	}
	
	public void initConfregConfig(String confregUrl) throws Exception {
		ConfregClient.registeConfregService(confregUrl, interfaceList);
	}
	
	public void startService() {
		
		LogUtil.i("Rpc Server Provider Started Successfully^+^");
		ServerUtil.accept(8888, new RequestCallBack() {

			public NetworkResponse onService(NetworkRequest networkRequest) throws Exception {
				RpcRequest rpcRequest = (RpcRequest)networkRequest.getRequestData();
				RpcResponse rpcResponse = new RpcResponse();
				try {

					LogUtil.i(String.format("Receive Rpc Request:(%s)", rpcRequest.toString()));
					Object serviceInstance = serviceMap.get(rpcRequest.getServiceName());

					Method method = methodMap.get(rpcRequest.getIdentifier());
					if (serviceInstance!=null && method!=null) {
						List parameterList = rpcRequest.getParameters();
						Object parameters[] = new Object[parameterList.size()];
						for (int i=0;i<parameterList.size();++i) {
							parameters[i] = parameterList.get(i);
						}

						Object returnValue = method.invoke(serviceInstance, parameters);
						rpcResponse.setData(returnValue);
						rpcResponse.setStatus(RpcResponse.STATUS_SUCCESS);
					}
				} catch(Exception e) {
					Throwable exception = e.getCause();
					rpcResponse.setStatus(RpcResponse.STATUS_FAILURE);
					rpcResponse.setExceptionClass(exception.getClass().getName());
					rpcResponse.setMessage(exception.getMessage());
				}
				return new NetworkResponse(rpcResponse);
			}
		});
	}
}