package com.stuben.monitop.server.dao.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.common.MonitorMsg;
import com.stuben.monitop.server.service.AlarmService;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(consumerGroup = "monitop-consumer", topic = MonitorConstant.MONITOR_SINK_TOPIC)
public class MonitorDataConsumer implements RocketMQListener<MonitorMsg> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlarmService alarmService;

    @Override
    public void onMessage(MonitorMsg monitorMsg) {
        logger.debug("MonitorMsg data : {}", monitorMsg);

        alarmService.checkMsg(monitorMsg);
    }
}
