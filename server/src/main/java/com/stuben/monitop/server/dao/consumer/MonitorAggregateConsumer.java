package com.stuben.monitop.server.dao.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.stuben.monitop.common.MonitorAggregate;
import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.server.service.AggregateService;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(consumerGroup = "monitop-consumer", topic = MonitorConstant.AGGREGATE_SINK_TOPIC)
public class MonitorAggregateConsumer implements RocketMQListener<MonitorAggregate> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AggregateService aggregateService;

    @Override
    public void onMessage(MonitorAggregate monitorAggregate) {
        logger.debug("MonitorAggregate data : {}", monitorAggregate);
        aggregateService.save(monitorAggregate);
    }
}
