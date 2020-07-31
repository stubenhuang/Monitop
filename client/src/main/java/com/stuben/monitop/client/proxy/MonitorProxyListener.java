package com.stuben.monitop.client.proxy;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.stuben.monitop.common.MonitorConstant;

@RocketMQMessageListener(topic = MonitorConstant.PROXY_INFO_REFRESH, consumerGroup = "monitop-client", selectorExpression = "${spring.application.name}")
public class MonitorProxyListener implements RocketMQListener<MonitorProxyInfoResult> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private MonitorProxyRefresher monitorProxyRefresher;

    @Override
    public void onMessage(MonitorProxyInfoResult monitorProxyInfoResult) {
        if (null == monitorProxyRefresher) {
            logger.warn("null monitorProxyRefresher");
        }

        monitorProxyRefresher.refreshMonitor(monitorProxyInfoResult);
    }

}
