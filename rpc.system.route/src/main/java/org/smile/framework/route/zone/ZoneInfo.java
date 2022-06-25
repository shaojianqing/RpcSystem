package org.smile.framework.route.zone;

import java.io.Serializable;

public class ZoneInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String idcName;
	
	private String cityName;
	
	private String rpcUrl;
	
	private String range;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcName() {
		return idcName;
	}

	public void setIdcName(String idcName) {
		this.idcName = idcName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRpcUrl() {
		return rpcUrl;
	}

	public void setRpcUrl(String rpcUrl) {
		this.rpcUrl = rpcUrl;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	
	public String toString() {
		return String.format("ZoneInfo[name:%s, range:%s, idcName:%s, cityName:%s, rpcUrl:%s]", this.name, this.range, this.idcName, this.cityName, this.rpcUrl);
	}
}
