package org.smile.framework.route;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RouteRuleProcessor {
	
	private static Map<String, IRuleGenerator> ruleGeneratorMap = new HashMap<String, IRuleGenerator>();
	
	public static String generateRouteRule(Method method, Object[] parameters) throws Exception {
		if (method!=null) {
			if (method.isAnnotationPresent(RouteRule.class)) {
				RouteRule routeRule = (RouteRule)method.getAnnotation(RouteRule.class);
				String generatorName = routeRule.ruleGenerator();
				IRuleGenerator ruleGenerator = null;
				if (ruleGeneratorMap.containsKey(generatorName)) {
					ruleGenerator = ruleGeneratorMap.get(generatorName);
				} else {
					ruleGenerator = (IRuleGenerator)Class.forName(generatorName).newInstance();
					ruleGeneratorMap.put(generatorName, ruleGenerator);
				}
				if (parameters!=null && parameters.length>0) {
					Object parameter = parameters[0];
					return ruleGenerator.generateRule(parameter);
				} else {
					throw new RuntimeException("传入路由规则计算参数不能为空!");
				}
			} else {
				return null;
			}
		} else {
			throw new RuntimeException("传入路由规则计算方法不能为空!");
		}
	}
}
