package com.stuben.monitop.common;

public class MonitorAggregate {
    private String app;
    private int pileNo;
    private long dateTimestamp;
    private int average;
    private int max;
    private int min;
    private int count;
    private int sum;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getPileNo() {
        return pileNo;
    }

    public void setPileNo(int pileNo) {
        this.pileNo = pileNo;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

}
