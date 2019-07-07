package com.aliware.tianchi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientStatus {
    private static final ConcurrentMap<String, ClientStatus> SERVICE_STATISTICS = new ConcurrentHashMap<>();

    final AtomicInteger activeCount = new AtomicInteger(0);
    final AtomicLong requestCount = new AtomicLong(0);
//    final AtomicLong responseCount = new AtomicLong(0);
    final AtomicInteger maxThread = new AtomicInteger(Integer.MAX_VALUE);
    private ClientStatus(){

    }

    public static ClientStatus requestCount(String ip,int port,boolean success) {
        ClientStatus clientStatus = getStatus(ip,port);
        clientStatus.requestCount.incrementAndGet();
        if(success) {
            clientStatus.activeCount.incrementAndGet();
        }else {
            clientStatus.activeCount.decrementAndGet();
        }
        return clientStatus;
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

    public static ClientStatus responseCount(String ip,int port,boolean fail) {
        ClientStatus clientStatus = getStatus(ip,port);
        if(fail){
            clientStatus.activeCount.decrementAndGet();
//            clientStatus.responseCount.incrementAndGet();
        }else {
            clientStatus.activeCount.decrementAndGet();
//            clientStatus.responseCount.incrementAndGet();
        }
        return clientStatus;
    }

    @Override
    public String toString() {
        return "ClientStatus{" +
                ", activeCount=" + activeCount.get() +
                ", requestCount=" + requestCount.get() +
//                ", responseCount=" + responseCount.get() +
                '}';
    }
}
