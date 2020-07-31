package com.stuben.monitop.server.service.strategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.stuben.monitop.common.MonitorMsg;
import com.stuben.monitop.server.dao.po.AlarmConfigPO;
import com.stuben.monitop.server.dao.redis.WarningRedis;
import com.stuben.monitop.server.enums.StrategyEnum;


public abstract class AbstractStrategy {

    @Autowired
    protected WarningRedis warningRedis;

    /**
     * 类型
     */
    public abstract StrategyEnum type();

    /**
     * 是否告警
     * 
     * @param alarmConfig 告警配置
     * @param monitorMsg 监控信息
     * @return 是否告警
     */
    public boolean isAlarm(AlarmConfigPO alarmConfig, MonitorMsg monitorMsg) {

        boolean isAlarm = false;

        if (isWarning(alarmConfig, monitorMsg)) {
            long warningCount = addWarningRecord(alarmConfig.getId(), monitorMsg.getNum());
            if (warningCount >= alarmConfig.getAlarmTrigger()) {
                isAlarm = true;
            }
        } else {
            clearWarningRecord(alarmConfig.getId());
        }

        return isAlarm;
    }

    /**
     * 获取告警记录并清空告警次数
     *
     * @param alarmConfigId 告警配置ID
     */
    public List<Integer> getAndClearWarningRecord(Integer alarmConfigId) {
        List<Integer> records = warningRedis.lrange(alarmConfigId, 0, -1);
        clearWarningRecord(alarmConfigId);
        return records;
    }

    /**
     * 是否预警
     *
     * @param alarmConfig 告警配置
     * @param monitorMsg 监控信息
     * @return 是否预警
     */
    abstract boolean isWarning(AlarmConfigPO alarmConfig, MonitorMsg monitorMsg);

    /**
     * 增加告警次数
     * 
     * @param alarmConfigId 告警配置ID
     * @param monitorNum 监控到的数量
     */
    private long addWarningRecord(Integer alarmConfigId, int monitorNum) {
        warningRedis.lpush(alarmConfigId, monitorNum);
        return warningRedis.llen(alarmConfigId);
    }

    /**
     * 清空告警次数
     * 
     * @param alarmConfigId 告警配置ID
     */
    private void clearWarningRecord(Integer alarmConfigId) {
        warningRedis.del(alarmConfigId);
    }
}
