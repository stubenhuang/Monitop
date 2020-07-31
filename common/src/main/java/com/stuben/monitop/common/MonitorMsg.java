package com.stuben.monitop.common;

import java.io.Serializable;

public class MonitorMsg implements Serializable {

    /**
     * app名称
     * 
     * 注:根据项目的spring.application.name注入, 自己set无效
     */
    private String app;

    /**
     * 桩号
     */
    private int pileNo;

    /**
     * 上报的数量
     */
    private int num;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public long getTimestamp() {
        return timestamp;
    }

    public int getPileNo() {
        return pileNo;
    }

    public void setPileNo(int pileNo) {
        this.pileNo = pileNo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MonitorMsg(int pileNo, int num) {
        this.pileNo = pileNo;
        this.num = num;
    }

    public MonitorMsg() {}


}
