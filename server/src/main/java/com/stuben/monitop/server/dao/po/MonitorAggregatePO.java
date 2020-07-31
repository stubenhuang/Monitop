package com.stuben.monitop.server.dao.po;

import java.util.Date;

import com.stuben.monitop.server.dao.elasticsearch.EsDeclare;
import org.springframework.data.annotation.Id;


@EsDeclare(index = "monitor_aggregate")
public class MonitorAggregatePO {
    @Id
    private String id;
    /**
     * 应用名
     */
    private String app;
    /**
     * 桩号
     */
    private int pileNo;
    /**
     * 平均数
     */
    private int average;
    /**
     * 最大值
     */
    private int max;
    /**
     * 最小值
     */
    private int min;
    /**
     * 数量
     */
    private int count;
    /**
     * 总值
     */
    private int sum;
    /**
     * 统计日期
     */
    private Date dayTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getDayTime() {
        return dayTime;
    }

    public void setDayTime(Date dayTime) {
        this.dayTime = dayTime;
    }
}
