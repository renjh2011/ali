package com.aliware.tianchi;

public class CustomWeight {
    private Integer port;
    private Integer weight;
    private Integer currentWeight;

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
}