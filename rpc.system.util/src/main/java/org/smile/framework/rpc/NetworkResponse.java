package org.smile.framework.rpc;

public class NetworkResponse<T> {

    private long sequence;

    private T responseData;

    public NetworkResponse(T responseData) {
        this.responseData = responseData;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }
}
