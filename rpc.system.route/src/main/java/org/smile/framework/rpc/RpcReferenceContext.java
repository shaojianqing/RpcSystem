package org.smile.framework.rpc;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.smile.framework.core.context.ApplicationContext;
import org.smile.framework.core.context.impl.ClassPathXmlApplicationContext;
import org.smile.framework.proxy.RpcProxy;
import org.smile.framework.proxy.factory.BeanProxyFactory;
import org.smile.framework.route.RouteRuleMapper;
import org.smile.framework.route.annotation.RpcReference;
import org.smile.framework.route.exception.RpcReferenceException;
import org.smile.framework.route.service.ServiceMetaData;
import org.smile.framework.route.service.ServiceMetaDataFactory;

public class RpcReferenceContext {
	
	private ApplicationContext context;
	
	private InputStream configInputStream;
	
	private List<Element> referenceList;
	
	private Document document;
	
	private RpcProxy rpcProxy;
	
	private ServiceMetaDataFactory serviceMetaDataFactory = new ServiceMetaDataFactory();
	
	private Map<String, String> namespaceMap = new HashMap<String, String>();
	
	private Map<String, Object> referenceMap = new HashMap<String, Object>();

	public RpcReferenceContext(ApplicationContext context, String configurationFileName) throws Exception {
		this.context = context;
		this.configInputStream = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configurationFileName);
		
		this.initRpcReferenceContext();
	}
	
	public void initRpcReference(String managerUrl, String confregUrl, String proxyUrl, String vipUrl, String configFilename) throws Exception {
		RouteRuleMapper.initRouteRuleMapper(managerUrl, 12000);
		RouteRuleMapper.intiConfregMapper(confregUrl, 16000);
		RouteRuleMapper.initVipAddressMapper(vipUrl, 1880);
		RouteRuleMapper.initProxyConfig(proxyUrl);
		ZoneContextUtil.initLocalZoneInfo(configFilename);
	}

	private void initRpcReferenceContext() throws Exception {
		SAXReader xmlReader = new SAXReader();
		
		namespaceMap.put("rpc","http://rpc.system.org/service");
		
		document = xmlReader.read(configInputStream);
        
        initReferenceContext(document);
	}
	
	@SuppressWarnings("unchecked")
	private void initReferenceContext(Document document) throws Exception {
		XPath xPath = document.createXPath("//rpc:reference");
        xPath.setNamespaceURIs(namespaceMap);
        
        rpcProxy = new RpcProxy(serviceMetaDataFactory);
        referenceList = xPath.selectNodes(document);
        if (referenceList!=null && referenceList.size()>0) {
        	for (Element referenceNode:referenceList) {
        		initReferenceMap(referenceNode);
        		initServiceMetaData(referenceNode);
        	}
        	
        	autowireRpcService();
        }
	}

	private void autowireRpcService() throws Exception {
		Collection<Object> serviceList = context.getBeanList();
		if (serviceList!=null && serviceList.size()>0) {
			for (Object service:serviceList) {
				Field[] fields = service.getClass().getDeclaredFields();
				for (Field field:fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(RpcReference.class)) {
						RpcReference rpcReference = (RpcReference)field.getAnnotation(RpcReference.class);
						String referenceId = rpcReference.referenceId();
						if (StringUtils.isBlank(referenceId)) {
							String message = String.format("ReferenceId can not be empty! className: fieldName:", service.getClass().getName(), field.getName());
							throw new RpcReferenceException(message);
						}
						
						if (referenceMap.containsKey(referenceId)==false) {
							String message = String.format("RpcProxy Reference does not exist!referenceId:", referenceId);
							throw new RpcReferenceException(message);
						}
						
						Object reference = referenceMap.get(referenceId);
						field.set(service, reference);
					}
				}
			}
		}
	}

	private void initReferenceMap(Element referenceNode) throws Exception {
		String id = referenceNode.attributeValue("id");
		String interfaze = referenceNode.attributeValue("interface");
		
		if (StringUtils.isBlank(id)) {
			throw new RpcReferenceException("Reference id can not be Empty!");
		}
		
		if (StringUtils.isBlank(interfaze)) {
			String message = String.format("Reference interface can not be Empty! Reference id:%s", id);
			throw new RpcReferenceException(message);
		}
		
		Class<?> clazz = Class.forName(interfaze);
		Class<?>[] interfaces = new Class<?>[]{clazz};
		Object reference = BeanProxyFactory.createProxy(interfaces, rpcProxy);
		referenceMap.put(id, reference);
	}
	
	@SuppressWarnings("unchecked")
	private void initServiceMetaData(Element referenceNode) throws Exception {
		String referenceId = referenceNode.attributeValue("id");
        String interfaze = referenceNode.attributeValue("interface");
 
        ServiceMetaData serviceMetaData = new ServiceMetaData();
		List<Element> vipList = referenceNode.elements("vip");
        if (vipList.size()>1) {
        	String message = String.format("Vip configuration can be empty or only one! ReferenceId:%s", referenceId);
        	throw new RpcReferenceException(message);
        }
        
        if (vipList.size()==1) {
        	Element vipConfig = vipList.get(0);
        	String only = vipConfig.attributeValue("only");
        	
        	boolean isOnly = Boolean.valueOf(only);
        	if (isOnly) {
        		List<Element> targetUrlList = vipConfig.elements("targetUrl");
        		if (targetUrlList==null || targetUrlList.size()==0) {
        			String message = String.format("Target Url can not be Empty! ReferenceId:%s", referenceId);
                	throw new RpcReferenceException(message);
        		}
        		
        		if (targetUrlList.size()>1) {
        			String message = String.format("Target Url can be only one! ReferenceId:%s", referenceId);
                	throw new RpcReferenceException(message);
        		}
        		
        		Element targetUrlElement = targetUrlList.get(0);
        		String targetUrl = targetUrlElement.attributeValue("url");
        		if (StringUtils.isBlank(targetUrl)) {
        			String message = String.format("Target Url value can not be empty:%s", referenceId);
                	throw new RpcReferenceException(message);
        		}
        		serviceMetaData.setTargetUrl(targetUrl);
        	}
        	serviceMetaData.setVipOnly(isOnly);
        }
        
        Class<?> clazz = Class.forName(interfaze);
        this.serviceMetaDataFactory.configServiceMeatData(clazz, serviceMetaData);
	}
}