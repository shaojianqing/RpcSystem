package org.smile.framework.route;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smile.framework.core.util.LogUtil;
import org.smile.framework.route.exception.RpcRouteException;
import org.smile.framework.route.service.ServiceMetaData;
import org.smile.framework.route.zone.ZoneInfo;
import org.smile.framework.rpc.NetworkRequest;
import org.smile.framework.rpc.NetworkSession;
import org.smile.framework.rpc.ZoneContextUtil;
import org.smile.framework.rpc.data.RpcRequest;
import org.smile.framework.util.IdentifierUtil;
import org.smile.framework.util.NetworkUtil;

public class RouteRuleMapper {
	
	private static String proxyUrl;
	
	private static Map<Integer, ZoneInfo> zoneInfoMap = new HashMap<Integer, ZoneInfo>();
	
	private static Map<String, List<AddressInfo>> addressInfoMap = new HashMap<String, List<AddressInfo>>();
	
	private static Map<String, String> vipAddressMapInfo = new HashMap<String, String>();
	
	@SuppressWarnings("unchecked")
	public static void initRouteRuleMapper(String url, int port) throws Exception {
		String zoneName = "zoneName=RZ002";

		NetworkRequest networkRequest = new NetworkRequest(zoneName);
		NetworkSession networkSession = new NetworkSession(networkRequest);

		NetworkUtil.sendRequest(url, port, networkSession);

		List<ZoneInfo> zoneInfoList = (List<ZoneInfo>)networkSession.getNetworkResponse().getResponseData();
		if (zoneInfoList!=null && zoneInfoList.size()>0) {
			for (ZoneInfo zoneInfo:zoneInfoList) {
				if (zoneInfo.getRange()!=null) {
					String range = zoneInfo.getRange();
					int index = range.indexOf('-');
					Integer start = Integer.valueOf(range.substring(0, index));
					Integer end = Integer.valueOf(range.substring(index+1));
					for (int i=start;i<=end;++i) {
						zoneInfoMap.put(i, zoneInfo);
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void intiConfregMapper(String confregUrl, int port) throws Exception {
		
		OperationRequest operationRequest = new OperationRequest();
		operationRequest.setMethod(OperationRequest.METHOD_SUBSCRIBE);

		NetworkRequest networkRequest = new NetworkRequest(operationRequest);
		NetworkSession networkSession = new NetworkSession(networkRequest);

		NetworkUtil.sendRequest(confregUrl, port, networkSession);

		addressInfoMap = (Map<String, List<AddressInfo>>)networkSession.getNetworkResponse().getResponseData();
	}
	
	public static void initProxyConfig(String proxyUrlp) {
		proxyUrl = proxyUrlp;
	}
	
	@SuppressWarnings("unchecked")
	public static void initVipAddressMapper(String vipUrl, int port) throws Exception {
		String configMap = "requestVipAddressMap";

		NetworkRequest networkRequest = new NetworkRequest(configMap);
		NetworkSession networkSession = new NetworkSession(networkRequest);

		NetworkUtil.sendRequest(vipUrl, port, networkSession);
		vipAddressMapInfo = (Map<String, String>)networkSession.getNetworkResponse().getResponseData();
	}
	
	@SuppressWarnings("rawtypes")
	public static RpcRequest routeRpcContext(ServiceMetaData serviceMetaData, String rule, Method method) {
		RpcRequest context = new RpcRequest();
		if (rule!=null) {
			if (rule.length()==2) {
				int ruleNum = Integer.valueOf(rule);
				if (serviceMetaData.isVipOnly()) {
					prepareRpcContextWithVip(context, serviceMetaData);
				} else {
					ZoneInfo targetZoneInfo = zoneInfoMap.get(ruleNum);
					if (targetZoneInfo!=null) {
						if (ZoneContextUtil.checkoutSameCity(targetZoneInfo)) {
							if (ZoneContextUtil.checkoutSameIdc(targetZoneInfo)) {
								if (ZoneContextUtil.checkoutSameZone(targetZoneInfo)) {
									prepareRpcContextWithZoneConfreg(context, method);
								} else {
									prepareRpcContextWithIDCConfreg(context, method);
								}
							} else {
								prepareRpcContextWithZoneInfo(context, targetZoneInfo);
							}
						} else {
							prepareRpcContextWithProxy(context, targetZoneInfo);
						}
						context.setServerUrl(targetZoneInfo.getRpcUrl());
					} else {
						throw new RuntimeException("未能找到指定ruleNum的ZoneInfo");
					}
				}
			}
		}
		return context;
	}

	private static void prepareRpcContextWithProxy(RpcRequest context, ZoneInfo zoneInfo) {
		context.setProxyUrl(proxyUrl);
		context.setIsProxied(true);
		context.setServerUrl(zoneInfo.getRpcUrl());
		LogUtil.i(String.format("Select Proxy Access Route Method! ServerUrl:%s, ProxyUrl:%s", zoneInfo.getRpcUrl(), proxyUrl));
	}

	private static void prepareRpcContextWithVip(RpcRequest context, ServiceMetaData serviceMetaData) {
		String serverUrl = serviceMetaData.getTargetUrl();
		if (vipAddressMapInfo.containsKey(serverUrl)) {
			String serverIp = vipAddressMapInfo.get(serverUrl);
			context.setServerUrl(serverIp);
			LogUtil.i(String.format("Select VIP Address Route Method! ServerUrl:%s, ServerIp:%s", serverUrl, serverIp));
		} else {
			String message = String.format("can not find the url mapping ip address! url:%s", serverUrl);
			throw new RpcRouteException(message);
		}
	}
	
	private static void prepareRpcContextWithZoneInfo(RpcRequest context, ZoneInfo targetZoneInfo) {
		String serverUrl = targetZoneInfo.getRpcUrl();
		if (vipAddressMapInfo.containsKey(serverUrl)) {
			String serverIp = vipAddressMapInfo.get(serverUrl);
			context.setServerUrl(serverIp);
			LogUtil.i(String.format("Select Zone Url Mapping Route Method! ServerUrl:%s, ServerIp:%s", serverUrl, serverIp));
		} else {
			String message = String.format("can not find the url mapping ip address! url:%s", serverUrl);
			throw new RpcRouteException(message);
		}
	}

	private static void prepareRpcContextWithZoneConfreg(RpcRequest context, Method method) {
		String identifier = IdentifierUtil.generateIdentifier(method);
		if (addressInfoMap.containsKey(identifier)) {
			List<AddressInfo> addressInfoList = addressInfoMap.get(identifier);
			if (addressInfoList.size()>0) {
				int randomIndex = 0;
				AddressInfo addressInfo = addressInfoList.get(randomIndex);
				context.setServerUrl(addressInfo.getIpAddress());
				LogUtil.i(String.format("Select Zone Confreg Route Method! ServerIp:%s", addressInfo.getIpAddress()));
			} else {
				throw new RpcRouteException("未能从Confreg找到对应的服务");
			}
		} else {
			throw new RpcRouteException("未能从Confreg找到对应的服务");
		}
	}
	
	private static void prepareRpcContextWithIDCConfreg(RpcRequest context, Method method) {
		String identifier = IdentifierUtil.generateIdentifier(method);
		if (addressInfoMap.containsKey(identifier)) {
			List<AddressInfo> addressInfoList = addressInfoMap.get(identifier);
			if (addressInfoList.size()>0) {
				int randomIndex = 0;
				AddressInfo addressInfo = addressInfoList.get(randomIndex);
				context.setServerUrl(addressInfo.getIpAddress());
				LogUtil.i(String.format("Select IDC Confreg Route Method! ServerIp:%s", addressInfo.getIpAddress()));
			} else {
				throw new RuntimeException("未能从Confreg找到对应的服务");
			}
		} else {
			throw new RuntimeException("未能从Confreg找到对应的服务");
		}
	}
}
