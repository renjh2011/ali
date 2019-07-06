/*
package com.aliware.tianchi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientStatus {
    private static final ConcurrentMap<String, ClientStatus> SERVICE_STATISTICS = new ConcurrentHashMap<>();

    final AtomicInteger failed = new AtomicInteger(0);
    final AtomicInteger activeCount = new AtomicInteger(0);
    final AtomicLong requestCount = new AtomicLong(0);
    final AtomicLong responseCount = new AtomicLong(0);
    final AtomicLong failedTime = new AtomicLong();
//    final AtomicLong lastFailedTime = new AtomicLong();
    private ClientStatus(){

    }

    public static void requestCount(String ip,int port) {
        ClientStatus clientStatus = getStatus(ip,port);
        clientStatus.activeCount.incrementAndGet();
        clientStatus.requestCount.incrementAndGet();
    }

    public static ClientStatus getStatus(String ip,int port) {
        String key = ip+port;
        ClientStatus status = SERVICE_STATISTICS.get(key);
        if (status == null) {
            SERVICE_STATISTICS.putIfAbsent(key, new ClientStatus());
            status = SERVICE_STATISTICS.get(key);
        }
        return status;
    }

    public static void responseCount(String ip,int port,boolean fail) {
        ClientStatus clientStatus = getStatus(ip,port);
        if(fail){
            clientStatus.activeCount.decrementAndGet();
            clientStatus.responseCount.incrementAndGet();
            clientStatus.failed.incrementAndGet();
//            clientStatus.lastFailedTime.set(clientStatus.failedTime.get());
            clientStatus.failedTime.set(System.currentTimeMillis());
        }else {
            clientStatus.activeCount.decrementAndGet();
            clientStatus.responseCount.incrementAndGet();
        }

    }

    @Override
    public String toString() {
        return "ClientStatus{" +
                "failed=" + failed.get() +
                ", activeCount=" + activeCount.get() +
                ", requestCount=" + requestCount.get() +
                ", responseCount=" + responseCount.get() +
                '}';
    }
}
*/
