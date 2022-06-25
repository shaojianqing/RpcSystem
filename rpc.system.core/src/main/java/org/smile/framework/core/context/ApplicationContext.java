package org.smile.framework.core.context;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.smile.framework.core.exception.BeanCreationException;
import org.smile.framework.core.exception.BeanDefinitionException;
import org.smile.framework.core.exception.BeanInjectionException;
import org.smile.framework.core.factory.BeanFactory;
import org.smile.framework.core.util.ClassUtil;
import org.smile.framework.core.util.ListUtil;

public abstract class ApplicationContext implements BeanFactory {
	
	protected String configPath;
	
	protected InputStream inputStream;

	protected Map<String, Object> beanMap = new HashMap<String, Object>();
	
	protected Map<String, String> initMethodMap =  new HashMap<String, String>();
	
	private List<Element> nodeList;
	
	@SuppressWarnings("unchecked")
	protected void initContext() throws Exception  {
		SAXReader xmlReader = new SAXReader();
        
        Document document = xmlReader.read(inputStream);
        Element root = document.getRootElement();
        
		nodeList = root.elements("bean");
	}
	
	protected void initBeanMap() throws Exception {
		
        for (Element node:nodeList) {
        	instantiateBean(node);      	
        }        
	}
	
	protected void initBeanProperty() throws Exception {
	
		for (Element node:nodeList) {
			wireBeanPropertys(node);
        }
	}
	
	protected void instantiateBean(Element node) throws Exception {
		if (node!=null) {
			String beanName = node.attributeValue("id");
        	String clazzName = node.attributeValue("class");
        	String initMethod = node.attributeValue("init-method");
        	
        	if (StringUtils.isBlank(beanName)) {
        		throw new BeanDefinitionException("Bean Id can not be empty!");
        	}
        	
        	if (StringUtils.isBlank(clazzName)) {
        		throw new BeanDefinitionException("Bean Class can not be empty!");
        	}
        	
        	Class<?> clazz = Class.forName(clazzName);
        	Object beanObject = clazz.newInstance();
        	beanMap.put(beanName, beanObject);
        	
        	if (StringUtils.isNotBlank(initMethod)) {
        		initMethodMap.put(beanName, initMethod);
        	}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void wireBeanPropertys(Element node) throws Exception {
		if (node!=null) {
			String beanName = node.attributeValue("id");
			
			if (!beanMap.containsKey(beanName)) {
				throw new BeanCreationException(String.format("Bean does not exist! beanName:%s", beanName));
			}
        	
			Object bean = beanMap.get(beanName);
			Map<String, Method> methodMap = ClassUtil.prepareMethodMap(bean.getClass());
			
			List<Element> properties = node.elements("property");
			if (properties!=null && properties.size()>0) {
				for (Element property:properties) {
					wireBeanProperty(bean, methodMap, property);
				}
			}        	
		}		
	}
	
	protected void runInitMethod() throws Exception {
		if (initMethodMap!=null && initMethodMap.size()>0) {
			Set<String> keySet = initMethodMap.keySet();
			for (String key:keySet) {
				Object bean = beanMap.get(key);
				if (bean!=null) {
					String methodName = initMethodMap.get(key);
					Method initMethod = ClassUtil.getMethodByName(bean.getClass(), methodName);
					Object param[] = null;
					initMethod.invoke(bean, param);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void wireBeanProperty(Object bean, Map<String, Method> methodMap, Element property)
			throws Exception {
		String propertyName = property.attributeValue("name");
		String propertyRef = property.attributeValue("ref");
		String propertyValue = property.attributeValue("value");
		
		if (StringUtils.isBlank(propertyName)) {
			throw new BeanInjectionException("Property Name can not be empty!");
		}
		
		String writeMethodName = ClassUtil.getWriteMethodName(propertyName);
		
		if (!methodMap.containsKey(writeMethodName)) {
			String message = String.format("Bean %s does not contain write method:%s !", bean.getClass().getName(), writeMethodName);
			throw new BeanInjectionException(message);
		}
		
		Method writeMethod = methodMap.get(writeMethodName);					
		if (StringUtils.isNotBlank(propertyRef)) {
			Object propertyObject = beanMap.get(propertyRef);
			if (propertyObject==null) {
				throw new BeanInjectionException(String.format("The Injection Property can not be null, Property Name:%s", propertyName));
			}
			
			writeMethod.invoke(bean, propertyObject);
		} else if (StringUtils.isNotBlank(propertyValue)){
			writeMethod.invoke(bean, propertyValue);
		} else {
			Element list = property.element("list");
			if (list!=null) {
				List<Element> values = list.elements("value");
				List<Element> refs = list.elements("ref");
				
				if (ListUtil.isNotEmpty(values) && ListUtil.isNotEmpty(refs)) {
					throw new BeanInjectionException("Bean property can only be injected for one Type!");
				}
				
				if (values!=null) {
					List<String> valueList = new ArrayList<String>();
					for (Element value:values) {
						valueList.add(value.getText());
					}
					writeMethod.invoke(bean, valueList);
				} else if (refs!=null) {
					@SuppressWarnings("rawtypes")
					List refList = new ArrayList();
					for(Element ref:refs) {
						String refName = ref.attributeValue("bean");
						if (!beanMap.containsKey(refName)) {
							throw new BeanInjectionException(String.format("Bean reference does not exist! bean name:%s", refName));
						}
						
						Object object = beanMap.get(refName);
						refList.add(object);
					}
					writeMethod.invoke(bean, refList);
				}
			}
		}
	}	
	
	public Object getBean(String id) {
		return beanMap.get(id);
	}
	
	public Collection<Object> getBeanList() {
		return beanMap.values();
	}
}
