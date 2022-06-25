package org.smile.framework.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class IdentifierUtil {
	
	public static String generateServiceName(Class<?> clazz) {
		if (clazz!=null) {
			return clazz.getCanonicalName();
		}
		return null;
	}

	public static String generateIdentifier(Method method) {
		if (method!=null) {
			Class<?> clazz = method.getDeclaringClass();
			String interfaceName = clazz.getName();
			String methodName = method.getName();
			Parameter parameters[] = method.getParameters();
			StringBuilder paramBuilder = new StringBuilder();
			boolean hasSeparator = false;
			for(Parameter parameter:parameters) {
				if (!hasSeparator) {
					hasSeparator = true;
				} else {
					paramBuilder.append(",");
				}
				paramBuilder.append(parameter.getType().getCanonicalName());				
			}
			
			return String.format("%s.%s(%s)", interfaceName, methodName, paramBuilder.toString());
		}
		return null;
	}
}
