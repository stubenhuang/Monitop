package com.stuben.monitop.server.service.strategy;

import com.stuben.monitop.common.MonitorMsg;
import com.stuben.monitop.server.dao.po.AlarmConfigPO;
import com.stuben.monitop.server.enums.StrategyEnum;
import org.springframework.stereotype.Service;


@Service
public class GreaterThanValueStrategy extends AbstractStrategy {
    @Override
    public StrategyEnum type() {
        return StrategyEnum.GREATER_THAN_VALUE;
    }

    @Override
    boolean isWarning(AlarmConfigPO alarmConfig, MonitorMsg monitorMsg) {
        return monitorMsg.getNum() >= alarmConfig.getWarningThreshold();
    }
}
