package org.smile.framework.rpc;

public class NetworkRequest<T> {

    private long sequence;

    private T requestData;

    public NetworkRequest(T requestData) {
        this.requestData = requestData;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public T getRequestData() {
        return requestData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }
}
