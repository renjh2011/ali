package com.aliware.tianchi;

import java.util.*;


public class CustomRobin {
//    private volatile static Map<Integer, Integer> orginalMap = new ConcurrentHashMap<>(3);
    private volatile static List<CustomWeight> customWeights = new ArrayList<>(3);
    public static void init(Map<Integer, CustomWeight> newMap){
        customWeights = new ArrayList<>(newMap.values());
        Collections.sort(customWeights);
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
