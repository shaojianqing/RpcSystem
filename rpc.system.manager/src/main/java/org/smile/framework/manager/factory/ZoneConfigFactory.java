package org.smile.framework.manager.factory;

import java.util.ArrayList;
import java.util.List;

import org.smile.framework.route.zone.ZoneInfo;

public class ZoneConfigFactory {
	
	public static List<ZoneInfo> prepareZoneInfoConfigData() {
		
		List<ZoneInfo> zoneInfoList = new ArrayList<ZoneInfo>();

		ZoneInfo zoneInfo = new ZoneInfo();
		zoneInfo.setCityName("hz");
		zoneInfo.setIdcName("ZTA");
		zoneInfo.setName("RZ01");
		zoneInfo.setRange("00-19");
		zoneInfo.setRpcUrl("rz01.system.org");
		zoneInfoList.add(zoneInfo);
		
		zoneInfo = new ZoneInfo();
		zoneInfo.setCityName("sh");
		zoneInfo.setIdcName("ETA");
		zoneInfo.setName("RZ02");
		zoneInfo.setRange("20-39");
		zoneInfo.setRpcUrl("rz02.system.org");
		zoneInfoList.add(zoneInfo);
		
		zoneInfo = new ZoneInfo();
		zoneInfo.setCityName("sh");
		zoneInfo.setIdcName("ETA");
		zoneInfo.setName("RZ03");
		zoneInfo.setRange("40-59");
		zoneInfo.setRpcUrl("rz03.system.org");
		zoneInfoList.add(zoneInfo);
		
		zoneInfo = new ZoneInfo();
		zoneInfo.setCityName("sh");
		zoneInfo.setIdcName("EUD");
		zoneInfo.setName("RZ04");
		zoneInfo.setRange("60-79");
		zoneInfo.setRpcUrl("rz04.system.org");
		zoneInfoList.add(zoneInfo);
		
		zoneInfo = new ZoneInfo();
		zoneInfo.setCityName("hz");
		zoneInfo.setIdcName("ZUE");
		zoneInfo.setName("RZ05");
		zoneInfo.setRange("80-99");
		zoneInfo.setRpcUrl("rz05.system.org");
		zoneInfoList.add(zoneInfo);
		
		return zoneInfoList;
	}
}
