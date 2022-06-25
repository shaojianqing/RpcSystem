package org.smile.framework.route.generator;

import java.util.UUID;

public class UUIDGenerator {
	
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString(); 
		uuid = uuid.replace("-", "");
		return uuid;
	}
}
