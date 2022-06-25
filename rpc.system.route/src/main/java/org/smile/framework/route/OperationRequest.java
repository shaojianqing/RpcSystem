package org.smile.framework.route;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OperationRequest<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String METHOD_REGISTER = "register_address";
	
	public static final String METHOD_SUBSCRIBE = "subscribe_address";
	
	private String method;
	
	private T data;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}