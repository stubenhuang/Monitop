package com.stuben.monitop.server.enums;

import java.util.stream.Stream;

public enum StrategyEnum {

    UNKNOWN(0, "未知"),

    LESS_THAN_VALUE(1, "小于等于值"),

    GREATER_THAN_VALUE(2, "大于等于值"),

    DECREASE_LAST_WEEK_PERCENT(3, "同比上周减少百分比"),

    INCREASE_LAST_WEEK_PERCENT(4, "同比上周增加百分比"),

    DECREASE_YESTERDAY_PERCENT(5, "环比昨天减少百分比"),

    INCREASE_YESTERDAY_PERCENT(6, "环比昨天增加百分比"),

    ;

    private int id;
    private String mean;

    StrategyEnum(int id, String mean) {
        this.id = id;
        this.mean = mean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public static StrategyEnum of(int warningStrategy) {
        return Stream.of(values()).filter(strategyEnum -> strategyEnum.id == warningStrategy).findFirst().orElse(UNKNOWN);
    }
}
