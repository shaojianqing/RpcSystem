package org.smile.framework.route.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceMetaDataFactory {
	
	private Map<Class<?>, ServiceMetaData> serviceMetaDataMap = new HashMap<Class<?>, ServiceMetaData>();
	
	public void configServiceMeatData(Class<?> clazz, ServiceMetaData serviceMetaData) {
		if (clazz!=null && serviceMetaData!=null) {
			this.serviceMetaDataMap.put(clazz, serviceMetaData);
		}
	}
	
	public ServiceMetaData getServiceMetaDataByInterface(Class<?> clazz) {
		return serviceMetaDataMap.get(clazz);
	}
}