package org.rpc.system.vip.mapper;

import java.util.HashMap;
import java.util.Map;

public class VipAddressMapper {
	
	private static Map<String, String> vipAddressMap = new HashMap<String, String>();
	
	public static void initAddressMapInfo() {
		vipAddressMap.put("rz01.system.org", "127.0.0.1");
		vipAddressMap.put("rz02.system.org", "127.0.0.1");
		vipAddressMap.put("rz03.system.org", "127.0.0.1");
		vipAddressMap.put("rz04.system.org", "127.0.0.1");
		vipAddressMap.put("rz05.system.org", "127.0.0.1");
	}

	public static Map<String, String> getVipAddressMap() {
		return vipAddressMap;
	}
}
