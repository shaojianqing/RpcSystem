package org.smile.framework.rpc;

public class NetworkSession {

    private NetworkRequest networkRequest;

    private NetworkResponse networkResponse;

    private boolean finish = false;

    private long startTime = System.currentTimeMillis();

    public NetworkSession(NetworkRequest networkRequest) {
        this.networkRequest = networkRequest;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public NetworkRequest getNetworkRequest() {
        return networkRequest;
    }

    public void setNetworkRequest(NetworkRequest networkRequest) {
        this.networkRequest = networkRequest;
    }

    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }
}
