package org.smile.framework.core.generator;

import java.util.UUID;

public class UUIDGenerator {
	
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
}
