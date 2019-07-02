package com.aliware.tianchi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomRobin {
    private volatile static Map<Integer, Integer> orginalMap = new ConcurrentHashMap<>(3);
    private volatile static List<CustomWeight> customWeights = new ArrayList<>(3);
    public synchronized static void init(Map<Integer, Integer> newMap){
        if(orginalMap.size()!=newMap.size()){
            System.out.println("newMap = [" + newMap + "]");
            orginalMap=newMap;
            Iterator<Map.Entry<Integer, Integer>> iterator = newMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer, Integer> entry = iterator.next();
                CustomWeight customWeight = new CustomWeight(entry.getKey(),entry.getValue(),0);
                customWeights.add(customWeight);
            }
        }
    }

    public static Integer getServer() {
        /// 原始权重之和
        Integer weightSum = 0;
        /// 最大当前权重对象
        CustomWeight maxCustomWeight = null;
        for(int i=0;i<customWeights.size();i++){
            CustomWeight customWeight = customWeights.get(i);
            if(customWeight!=null){
                weightSum+=customWeight.getWeight();
            }
            if(maxCustomWeight==null){
                maxCustomWeight=customWeight;
            }
            if(customWeight.getCurrentWeight()>maxCustomWeight.getCurrentWeight()){
                maxCustomWeight=customWeight;
            }
        }

        if(maxCustomWeight == null){
            return null;
        }
        maxCustomWeight.setCurrentWeight(maxCustomWeight.getCurrentWeight() - weightSum);

        for(int i=0;i<customWeights.size();i++){
            CustomWeight customWeight = customWeights.get(i);
            if(customWeight!=null){
                customWeight.setCurrentWeight(customWeight.getCurrentWeight()+customWeight.getWeight());
            }
        }
        return maxCustomWeight.getPort();
    }
}
