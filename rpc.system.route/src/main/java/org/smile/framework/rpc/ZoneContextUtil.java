package org.smile.framework.rpc;

import org.smile.framework.core.util.LogUtil;
import org.smile.framework.route.exception.RpcRouteException;
import org.smile.framework.route.zone.ZoneInfo;

public class ZoneContextUtil {
	
	private static ZoneInfo localZoneInfo;
	
	public static void initLocalZoneInfo(String fileName) {
		
		localZoneInfo = new ZoneInfo();		
		localZoneInfo.setCityName("sh");
		localZoneInfo.setIdcName("ETA");
		localZoneInfo.setName("RZ02");
		localZoneInfo.setRange("20-39");
		localZoneInfo.setRpcUrl("rz02.system.org");
		
		LogUtil.i("初始化当前ZoneInfo信息："+localZoneInfo);
	}
	
	public static ZoneInfo getLocalZoneInfo() {
		return localZoneInfo;
	}
	
	public static boolean checkoutSameCity(ZoneInfo targetZoneInfo) {
		if (targetZoneInfo!=null) {
			return localZoneInfo.getCityName().equals(targetZoneInfo.getCityName());
		} else {
			throw new RpcRouteException("目标ZoneInfo不能为空哦!");
		}
	}
	
	public static boolean checkoutSameIdc(ZoneInfo targetZoneInfo) {
		if (targetZoneInfo!=null) {
			return localZoneInfo.getIdcName().equals(targetZoneInfo.getIdcName());
		} else {
			throw new RpcRouteException("目标ZoneInfo不能为空哦!");
		}
	}
	
	public static boolean checkoutSameZone(ZoneInfo targetZoneInfo) {
		if (targetZoneInfo!=null) {
			return localZoneInfo.getName().equals(targetZoneInfo.getName());
		} else {
			throw new RpcRouteException("目标ZoneInfo不能为空哦!");
		}
	}	
}
