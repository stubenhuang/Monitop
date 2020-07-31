package com.stuben.monitop.client.proxy;

import com.stuben.monitop.client.common.MonitorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class MonitorProxyRefresher {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    public MonitorProxyRefresher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    synchronized void refreshMonitor(MonitorProxyInfoResult result) {
        try {
            doRefresh(result);
        } catch (Exception e) {
            logger.error("refreshMonitor error", e);
        }
    }

    private void doRefresh(MonitorProxyInfoResult result) throws ClassNotFoundException {
        if (null == result) {
            logger.error("null result");
            return;
        }

        MonitorProxyInfo proxyInfo = MonitorUtils.convertFromResult(result);

        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ((GenericApplicationContext) applicationContext).getBeanFactory();
        String[] beanNamesForType = defaultListableBeanFactory.getBeanNamesForType(proxyInfo.getClz());

        if (beanNamesForType.length != 1) {
            logger.error("Found class length is illegal , length:{}", beanNamesForType.length);
            return;
        }

        String beanName = beanNamesForType[0];
        Object bean = applicationContext.getBean(beanName);
        MonitorProxyAdvisor proxyAdvisor = MonitorProxyAdvisor.init(proxyInfo, applicationContext);

        if (!(bean instanceof Advised)) {
            logger.warn("it is not an advised bean , can`t add monitor proxy");
            return;
        }

        // 更新装饰器
        Advised advised = (Advised) bean;
        int index = advised.indexOf(proxyAdvisor);
        if (-1 != index) {
            advised.removeAdvisor(index);
        }

        if (proxyInfo.getEnable()) {
            advised.addAdvisor(proxyAdvisor);
        }

        logger.info("refresh success , MonitorProxyInfo : {} , BeanName:{}", proxyInfo, beanName);
    }


}
