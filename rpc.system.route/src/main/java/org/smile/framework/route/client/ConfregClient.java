package org.smile.framework.route.client;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.smile.framework.route.AddressInfo;
import org.smile.framework.route.OperationRequest;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkSession;
import org.smile.framework.util.IdentifierUtil;
import org.smile.framework.util.NetworkUtil;
import org.smile.framework.util.ProtocolUtil;

public class ConfregClient {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registeConfregService(String confregUrl, List<Class<?>> interfaceList) throws Exception {
		
		List<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
		
		if (interfaceList!=null && interfaceList.size()>0) {
			for (Class<?> interfaze:interfaceList) {
				List<AddressInfo> addressList = registeInterface(interfaze);
				addressInfoList.addAll(addressList);
			}
		}
		
		OperationRequest operationRequest = new OperationRequest();
		operationRequest.setMethod(OperationRequest.METHOD_REGISTER);
		operationRequest.setData(addressInfoList);

		NetworkRequest networkRequest = new NetworkRequest(operationRequest);
		NetworkSession networkSession = new NetworkSession(networkRequest);

		NetworkUtil.sendRequest(confregUrl, 16000, networkSession);
	}
	
	private static List<AddressInfo> registeInterface(Class<?> interfaze) {
		if (interfaze!=null) {
			Method[] methods = interfaze.getMethods();
			List<AddressInfo> addressInfos = new ArrayList<AddressInfo>();
			for (Method method:methods) {
				AddressInfo addressInfo = new AddressInfo();
				addressInfo.setAppName("pcapplycore");
				addressInfo.setIpAddress("127.0.0.1");
				addressInfo.setIdentifier(IdentifierUtil.generateIdentifier(method));
				addressInfo.setMethodName(method.getName());
				addressInfo.setInterfaceName(interfaze.getName());
				addressInfos.add(addressInfo);
			}
			return addressInfos;
		}
		return new ArrayList<AddressInfo>(); 
	}

}
