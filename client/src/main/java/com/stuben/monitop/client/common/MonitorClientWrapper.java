package com.stuben.monitop.client.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class MonitorClientWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorClientWrapper.class);

    private static MonitorClient monitorClient;

    public static MonitorClient get(ApplicationContext applicationContext) {
        if (null != monitorClient) {
            return monitorClient;
        }

        try {
            monitorClient = applicationContext.getBean(MonitorClient.class);
        } catch (Exception e) {
            LOGGER.warn("MonitorWrapper error", e);
            return null;
        }
        return monitorClient;
    }
}
