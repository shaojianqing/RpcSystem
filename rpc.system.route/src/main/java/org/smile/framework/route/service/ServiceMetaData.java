package org.smile.framework.route.service;

public class ServiceMetaData {
	
	private String targetUrl;
	
	private boolean vipOnly = false;

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public boolean isVipOnly() {
		return vipOnly;
	}

	public void setVipOnly(boolean vipOnly) {
		this.vipOnly = vipOnly;
	}
}
