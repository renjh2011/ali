package com.aliware.tianchi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class CustomRobin {
    private static final ConcurrentMap<Integer, CustomRobin> WEIGHT_STATISTICS = new ConcurrentHashMap<>();
    private AtomicInteger port = new AtomicInteger();
    private AtomicInteger weight = new AtomicInteger();
    private AtomicInteger currentWeight = new AtomicInteger();
    private CustomRobin(){

    }
    public static void init(Integer key,Integer port,Integer weight,Integer currentWeight){
        CustomRobin customRobin = WEIGHT_STATISTICS.get(key);
        if(customRobin==null) {
            customRobin = new CustomRobin();
            customRobin.port.set(port);
            customRobin.weight.set(weight);
            customRobin.currentWeight.set(currentWeight);
            WEIGHT_STATISTICS.putIfAbsent(key, customRobin);
        }
    }
    public static CustomRobin get(Integer key){
        init(key,key,50,0);
        return WEIGHT_STATISTICS.get(key);

    }
    public static void setWeight(Integer key,Integer weight){
        if(weight<=0){
            Iterator<Map.Entry<Integer,CustomRobin>> iterator = WEIGHT_STATISTICS.entrySet().iterator();
            while (iterator.hasNext()){
                CustomRobin customRobin =iterator.next().getValue();
                customRobin.weight.set(customRobin.weight.get()*10);
                customRobin.currentWeight.set(0);
                WEIGHT_STATISTICS.put(iterator.next().getKey(),customRobin);
            }
        }
        CustomRobin customRobin1 = WEIGHT_STATISTICS.get(key);
        if(customRobin1!=null){
            customRobin1.weight.set(weight);
            WEIGHT_STATISTICS.put(key,customRobin1);
            Iterator<Map.Entry<Integer,CustomRobin>> iterator = WEIGHT_STATISTICS.entrySet().iterator();
        }

    }

    public AtomicInteger getWeight() {
        return weight;
    }

    public AtomicInteger getCurrentWeight() {
        return currentWeight;
    }

    public void setWeight(Integer weight) {
        this.weight.set(weight);
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight.set(currentWeight);
    }

    public AtomicInteger getPort() {
        return port;
    }

    public static Integer getServer() {
        /// 原始权重之和
        Integer weightSum = 0;
        /// 最大当前权重对象
        CustomRobin maxCustomWeight = null;
        List<CustomRobin> customRobinList = new ArrayList<>(WEIGHT_STATISTICS.values());
        for(int i=0;i<customRobinList.size();i++){
            CustomRobin customRobin = customRobinList.get(i);
            if(customRobin!=null){
                weightSum+=customRobin.getWeight().get();
            }
            if(maxCustomWeight==null){
                maxCustomWeight=customRobin;
            }
            if(customRobin.getCurrentWeight().get()>maxCustomWeight.getCurrentWeight().get()){
                maxCustomWeight=customRobin;
            }
        }

        if(maxCustomWeight == null){
            return null;
        }
        maxCustomWeight.setCurrentWeight(maxCustomWeight.getCurrentWeight().get() - weightSum);

        for(int i=0;i<customRobinList.size();i++){
            CustomRobin customRobin = customRobinList.get(i);
            if(customRobin!=null){
                customRobin.setCurrentWeight(customRobin.getCurrentWeight().get()+customRobin.getWeight().get());
            }
        }
        return maxCustomWeight.getPort().get();
    }
}
