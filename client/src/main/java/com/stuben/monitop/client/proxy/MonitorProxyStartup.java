package com.stuben.monitop.client.proxy;

import java.util.List;

import com.stuben.monitop.client.common.IMonitorProxyFacade;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class MonitorProxyStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MonitorProxyRefresher monitorProxyRefresher;
    private String appName;

    @Reference
    private IMonitorProxyFacade monitorInfoFacade;

    public MonitorProxyStartup(MonitorProxyRefresher monitorProxyRefresher, String appName) {
        this.monitorProxyRefresher = monitorProxyRefresher;
        this.appName = appName;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("MonitorProxyStartup start...");

        try {
            List<MonitorProxyInfoResult> monitorProxyInfoResults = monitorInfoFacade.listProxys(appName);

            for (MonitorProxyInfoResult result : monitorProxyInfoResults) {
                monitorProxyRefresher.refreshMonitor(result);
            }

            logger.info("MonitorProxyStartup success...");

        } catch (Exception e) {
            logger.error("MonitorProxyStartup failed...");
        }
    }
}
