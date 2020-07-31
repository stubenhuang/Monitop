package com.stuben.monitop.client.premain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.stuben.monitop.client.common.MonitorClient;
import com.stuben.monitop.client.common.MonitorContext;
import com.stuben.monitop.common.MonitorMsg;

public class AgentMonitorInterceptor implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentMonitorInterceptor.class);

    private static MonitorClient monitorClient;

    public static void report(int pileNo, Object[] args) {
        if (null == monitorClient) {
            LOGGER.error("report error , monitor client is null");
            return;
        }

        Integer num = MonitorContext.getNum(pileNo, args);
        if (null != num) {
            monitorClient.report(new MonitorMsg(pileNo, num));
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AgentMonitorInterceptor.monitorClient = applicationContext.getBeansOfType(MonitorClient.class).values().stream().findFirst().orElse(null);
    }
}
