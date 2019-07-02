package com.aliware.tianchi;


public class CustomWeight implements Comparable<CustomWeight> {
    private volatile Integer port;
    private volatile Integer weight;
    private volatile Integer currentWeight;

    public CustomWeight(Integer port, Integer weight, Integer currentWeight) {
        this.port = port;
        this.weight = weight;
        this.currentWeight = currentWeight;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }

    @Override
    public int compareTo(CustomWeight o) {
        return this.getWeight()-o.getWeight();
    }
}