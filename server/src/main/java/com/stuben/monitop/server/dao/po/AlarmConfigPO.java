package com.stuben.monitop.server.dao.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 告警配置
 * </p>
 *
 */
@TableName("AlarmConfig")
public class AlarmConfigPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 告警名称
     */
    private String name;
    /**
     * 监听的APP
     */
    private String app;
    /**
     * 桩点
     */
    @TableField("pileNo")
    private Integer pileNo;
    /**
     * 预警策略 , see : com.stuben.monitop.server.enums.StrategyEnum
     */
    @TableField("warningStrategy")
    private Integer warningStrategy;
    /**
     * 预警阈值
     */
    @TableField("warningThreshold")
    private Integer warningThreshold;
    /**
     * 预警-->告警的触发次数
     */
    @TableField("alarmTrigger")
    private Integer alarmTrigger;
    /**
     * 告警的邮箱 , 用 , 分隔
     */
    @TableField("alarmEmails")
    private String alarmEmails;
    /**
     * 是否生效
     */
    private Integer enable;
    /**
     * 最近心跳时间
     */
    @TableField("lastHeartbeatTime")
    private Date lastHeartbeatTime;
    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getWarningStrategy() {
        return warningStrategy;
    }

    public void setWarningStrategy(Integer warningStrategy) {
        this.warningStrategy = warningStrategy;
    }

    public Integer getWarningThreshold() {
        return warningThreshold;
    }

    public void setWarningThreshold(Integer warningThreshold) {
        this.warningThreshold = warningThreshold;
    }

    public Integer getAlarmTrigger() {
        return alarmTrigger;
    }

    public void setAlarmTrigger(Integer alarmTrigger) {
        this.alarmTrigger = alarmTrigger;
    }

    public String getAlarmEmails() {
        return alarmEmails;
    }

    public void setAlarmEmails(String alarmEmails) {
        this.alarmEmails = alarmEmails;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Date getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(Date lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AlarmConfig{" + ", id=" + id + ", name=" + name + ", app=" + app + ", pileNo=" + pileNo + ", warningStrategy=" + warningStrategy
                + ", warningThreshold=" + warningThreshold + ", alarmTrigger=" + alarmTrigger + ", alarmEmails=" + alarmEmails + ", enable=" + enable
                + ", lastHeartbeatTime=" + lastHeartbeatTime + ", createTime=" + createTime + ", updateTime=" + updateTime + "}";
    }
}
