package com.stuben.monitop.server.dao.po;

import java.util.Date;

import com.stuben.monitop.server.dao.elasticsearch.EsDeclare;
import org.springframework.data.annotation.Id;


/**
 * 监控数据
 */
@EsDeclare(index = "monitor_data")
public class MonitorDataPO {
    @Id
    private String id;
    /**
     * 监控APP
     */
    private String app;
    /**
     * 监控桩号
     */
    private Integer pileNo;

    /**
     * 监控的时间戳
     */
    private Date minuteTime;

    /**
     * 监控到的数量
     */
    private Integer num;

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

    public Integer getPileNo() {
        return pileNo;
    }

    public void setPileNo(Integer pileNo) {
        this.pileNo = pileNo;
    }

    public Date getMinuteTime() {
        return minuteTime;
    }

    public void setMinuteTime(Date minuteTime) {
        this.minuteTime = minuteTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
