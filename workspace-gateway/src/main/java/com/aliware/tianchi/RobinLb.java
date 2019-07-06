package com.aliware.tianchi;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RobinLb {
    private AtomicInteger weight = new AtomicInteger(0);
    private AtomicInteger curWeight = new AtomicInteger(0);
    private AtomicInteger lastWeight = new AtomicInteger(0);
    private AtomicInteger port = new AtomicInteger(0);

    private RobinLb(Integer weight, Integer curWeight, Integer port) {
        this.weight.set(weight);
        this.lastWeight.set(weight);
        this.curWeight.set(curWeight);
        this.port.set(port);
    }

    private volatile static ConcurrentMap<Integer,RobinLb> SERVER_MAP =  new ConcurrentHashMap<>();
    static {
        SERVER_MAP.putIfAbsent(20880,new RobinLb(170,0,20880));
        SERVER_MAP.putIfAbsent(20870,new RobinLb(450,0,20870));
        SERVER_MAP.putIfAbsent(20890,new RobinLb(600,0,20890));
    }

    public void set(Integer weight,Integer port){
        SERVER_MAP.put(port, new RobinLb(weight,0,port));
        Iterator<Map.Entry<Integer,RobinLb>> iterator = SERVER_MAP.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,RobinLb> entry = iterator.next();
            entry.getValue().setCurWeight(0);
        }
    }
    public synchronized void fail(String ip,Integer port){
        ConcurrentMap<Integer,RobinLb> map = new ConcurrentHashMap(SERVER_MAP);
        Iterator<Map.Entry<Integer,RobinLb>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,RobinLb> entry = iterator.next();
            RobinLb robinLb = entry.getValue();
            ClientStatus clientStatus = ClientStatus.getStatus(ip,port);
            if(entry.getKey().equals(port)){
                robinLb.setWeight(robinLb.getWeight().get()-5);
                robinLb.setLastWeight(robinLb.getWeight().get());
                robinLb.setCurWeight(0);
                continue;
            }
            long time = System.currentTimeMillis()-clientStatus.lastFailedTime.get();
            if(time>500) {
                robinLb.setWeight(robinLb.getWeight().get() + 15);
            }
            robinLb.setLastWeight(robinLb.getWeight().get());
            entry.getValue().setCurWeight(0);
        }
        SERVER_MAP=map;
    }


    public static RobinLb getRobinLb(Integer port){
        RobinLb robinLb = SERVER_MAP.get(port);
        if (robinLb == null) {
            SERVER_MAP.putIfAbsent(port, new RobinLb(1,0,port));
            robinLb = SERVER_MAP.get(port);
        }
        return robinLb;
    }

    public static ConcurrentMap<Integer, RobinLb> getServerMap() {
        return SERVER_MAP;
    }

    public static void main(String[] args) {
        for(int i=0;i<25;i++) {
            getServer();
        }
    }
    public static Integer getServer(){
//        System.out.println(SERVER_MAP);
        Iterator<Map.Entry<Integer,RobinLb>> iterator = SERVER_MAP.entrySet().iterator();
        int maxWeight=Integer.MIN_VALUE;
        int totalWight = 0;
        RobinLb maxWeightRobinLb = null;
        while (iterator.hasNext()){
            Map.Entry<Integer,RobinLb> entry = iterator.next();
            int realWeight = entry.getValue().lastWeight.get();
            if(maxWeight<realWeight){
                maxWeight=entry.getValue().lastWeight.get();
                maxWeightRobinLb=entry.getValue();
            }
            totalWight+=entry.getValue().getWeight().get();
        }

        iterator = SERVER_MAP.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,RobinLb> entry = iterator.next();
            RobinLb robinLb = entry.getValue();
            if(maxWeightRobinLb.getPort().get()==robinLb.getPort().get()){
                int curTempWeight = robinLb.getWeight().get()+(robinLb.lastWeight.get()-totalWight);
                robinLb.setCurWeight(curTempWeight);
                robinLb.lastWeight.set(curTempWeight);
            }else {
                int curTempWeight = robinLb.lastWeight.get();
                robinLb.setCurWeight(robinLb.getWeight().get()+curTempWeight);
                robinLb.lastWeight.set(robinLb.getWeight().get()+curTempWeight);
            }

        }
        return maxWeightRobinLb.getPort().get();
//        System.out.println(maxWeightRobinLb);

    }

    public void setWeight(Integer weight) {
        this.weight.set(weight);
    }

    public void setCurWeight(Integer curWeight) {
        this.curWeight.set(curWeight);
    }

    public AtomicInteger getWeight() {
        return weight;
    }

    public AtomicInteger getCurWeight() {
        return curWeight;
    }

    public AtomicInteger getPort() {
        return port;
    }

    public Integer getLastWeight() {
        return lastWeight.get();
    }

    public void setLastWeight(Integer lastWeight) {
        this.lastWeight.set(lastWeight);
    }

    @Override
    public String toString() {
        return "RobinLb{" +
                "weight=" + weight +
                ", curWeight=" + curWeight +
                ", port=" + port +
                ", lastWeight=" + lastWeight +
                '}';
    }
}
