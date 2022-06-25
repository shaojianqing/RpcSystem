package org.smile.system.confreg;

import java.util.List;
import java.util.Map;

import org.smile.framework.callback.RequestCallBack;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.route.AddressInfo;
import org.smile.framework.route.OperationRequest;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkResponse;
import org.smile.framework.util.ServerUtil;
import org.smile.system.confreg.factory.ConfregMapper;

public class ConfregServer {
	
	public static void main(String[] args) {
		
		LogUtil.i("Confreg Service Started^+^");
		ServerUtil.accept(16000, new RequestCallBack(){

			public NetworkResponse onService(NetworkRequest networkRequest) throws Exception {
				@SuppressWarnings("rawtypes")
				OperationRequest request = (OperationRequest)networkRequest.getRequestData();
				LogUtil.i("Received Request Data From ZoneClient:");
				LogUtil.i(request.toString());

				if (OperationRequest.METHOD_REGISTER.equals(request.getMethod())) {
					List<AddressInfo> addressInfos = (List<AddressInfo>)request.getData();
					ConfregMapper.registeAddressInfo(addressInfos);
					NetworkResponse<Boolean> networkResponse = new NetworkResponse<Boolean>(true);
					return networkResponse;
				} else if (OperationRequest.METHOD_SUBSCRIBE.equals(request.getMethod())) {
					Map<String, List<AddressInfo>> addressInfoListMap = ConfregMapper.getAddressInfoMap();
					NetworkResponse<Map<String, List<AddressInfo>>> networkResponse = new NetworkResponse<Map<String, List<AddressInfo>>>(addressInfoListMap);
					return networkResponse;
				}

				NetworkResponse<byte[]> networkResponse = new NetworkResponse<byte[]>(new byte[]{0});
				return networkResponse;
			}
		});
	}
}
