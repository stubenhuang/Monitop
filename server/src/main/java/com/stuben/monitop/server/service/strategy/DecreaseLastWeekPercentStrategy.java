package com.stuben.monitop.server.service.strategy;

import com.stuben.monitop.common.MonitorMsg;
import com.stuben.monitop.server.dao.po.AlarmConfigPO;
import com.stuben.monitop.server.dao.po.MonitorAggregatePO;
import com.stuben.monitop.server.enums.StrategyEnum;
import com.stuben.monitop.server.service.AggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DecreaseLastWeekPercentStrategy extends AbstractStrategy {

    @Autowired
    private AggregateService aggregateService;

    @Override
    public StrategyEnum type() {
        return StrategyEnum.DECREASE_LAST_WEEK_PERCENT;
    }

    @Override
    boolean isWarning(AlarmConfigPO alarmConfig, MonitorMsg monitorMsg) {
        MonitorAggregatePO lastWeek = aggregateService.getLastWeek(monitorMsg.getApp(), monitorMsg.getPileNo(), monitorMsg.getTimestamp());

        if (null == lastWeek) {
            return false;
        }

        int percent = ((monitorMsg.getNum() - lastWeek.getAverage() * 100) / lastWeek.getAverage());

        return alarmConfig.getWarningThreshold() <= percent;
    }
}
