package com.stuben.monitop.client.common;

import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.common.MonitorMsg;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


public class MonitorClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    private String app;

    MonitorClient() {
        this.app = "none";
    }

    public MonitorClient(String app) {
        this.app = app;
    }

    /**
     * 上报信息
     */
    public void report(MonitorMsg msg) {
        try {
            msg.setApp(app);
            rocketMQTemplate.convertAndSend(MonitorConstant.SOURCE_TOPIC, msg);
        } catch (Throwable throwable) {
            logger.warn("report failed", throwable);
        }
    }

}
