package com.aliware.tianchi;

public class ProviderStatus {
    private volatile int port = -1;
    private volatile int activeCount = -1;
    private volatile int total = -1;
    private volatile long period;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
