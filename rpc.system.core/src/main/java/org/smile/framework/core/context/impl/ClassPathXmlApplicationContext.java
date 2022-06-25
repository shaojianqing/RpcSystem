package org.smile.framework.core.context.impl;

import org.smile.framework.core.context.ApplicationContext;

public class ClassPathXmlApplicationContext extends ApplicationContext {
	
	public ClassPathXmlApplicationContext(String fileName) throws Exception {
		
		this.configPath = fileName;
		
        this.inputStream = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configPath);
		
        this.initContext();
        this.initBeanMap();
        this.initBeanProperty();
        this.runInitMethod();
	}
}
