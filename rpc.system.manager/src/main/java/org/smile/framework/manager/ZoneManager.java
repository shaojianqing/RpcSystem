package org.smile.framework.manager;

import java.util.List;

import org.smile.framework.callback.RequestCallBack;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.manager.factory.ZoneConfigFactory;
import org.smile.framework.route.zone.ZoneInfo;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkResponse;
import org.smile.framework.util.ServerUtil;

public class ZoneManager {
	
	public static void main(String[] args) {
		
		LogUtil.i("ZoneManager Started^+^");
		ServerUtil.accept(12000, new RequestCallBack(){

			public NetworkResponse onService(NetworkRequest networkRequest) throws Exception {

				LogUtil.i("Received Request Data From ZoneClient!");
				List<ZoneInfo> zoneInfoList = ZoneConfigFactory.prepareZoneInfoConfigData();
				NetworkResponse networkResponse = new NetworkResponse(zoneInfoList);
				return networkResponse;
			}
		});
	}
}
