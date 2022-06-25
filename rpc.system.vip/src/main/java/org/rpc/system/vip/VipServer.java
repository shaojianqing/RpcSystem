package org.rpc.system.vip;

import java.util.Map;

import org.rpc.system.vip.mapper.VipAddressMapper;
import org.smile.framework.callback.RequestCallBack;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkResponse;
import org.smile.framework.util.ServerUtil;

public class VipServer {
	
	public static void main(String[] args) {
		
		LogUtil.i("VipServer Started^+^");
		VipAddressMapper.initAddressMapInfo();
		ServerUtil.accept(1880, new RequestCallBack(){

			public NetworkResponse onService(NetworkRequest networkRequest) throws Exception {
				LogUtil.i("Received Request Data From ZoneClient:");
				Map<String, String> addressMap = VipAddressMapper.getVipAddressMap();
				NetworkResponse networkResponse = new NetworkResponse(addressMap);
				return networkResponse;
			}
		});
	}
}
