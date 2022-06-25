package org.smile.system.confreg.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.framework.route.AddressInfo;

public class ConfregMapper {
	
	private static Map<String, List<AddressInfo>> addressInfoListMap = new HashMap<String, List<AddressInfo>>();
	
	public static void registeAddressInfo(List<AddressInfo> addressInfos) {
		if (addressInfos!=null) {
			for(AddressInfo addressInfo:addressInfos) {
				if (!addressInfoListMap.containsKey(addressInfo.getIdentifier())) {
					addressInfoListMap.put(addressInfo.getIdentifier(), new ArrayList<AddressInfo>());
				}
				List<AddressInfo> addressInfoList = addressInfoListMap.get(addressInfo.getIdentifier());
				addressInfoList.add(addressInfo);
			}
		}
	}
	
	public static Map<String, List<AddressInfo>> getAddressInfoMap() {
		return addressInfoListMap;
	}
}
