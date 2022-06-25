package org.smile.framework.proxy.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class BeanProxyFactory {
	
	public static Object createProxy(Class<? extends Object>[] interfaces, InvocationHandler handler) {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, handler);
	}
}
