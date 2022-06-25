package org.smile.framework.rpc.data;

import java.io.Serializable;

public class RpcResponse<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final short STATUS_FAILURE = 0;
	
	public static final short STATUS_SUCCESS = 1;
	
	private short status;
	
	private T data;
	
	private String message;
	
	private String exceptionClass;

	public boolean isSuccess() {
		return (STATUS_SUCCESS==status);
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}
}
