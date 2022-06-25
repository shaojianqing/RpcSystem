package org.smile.framework.core.util;

import org.apache.commons.lang3.StringUtils;

public class AssertUtil {
	
	public static void checkNotNull(Object object, String message) {
		if (object==null) {
			throw new NullPointerException(message);
		}
	}
	
	public static void checkNotBlank(String object, String message) {
		if (StringUtils.isBlank(object)) {
			throw new RuntimeException(message);
		}
	}
}
