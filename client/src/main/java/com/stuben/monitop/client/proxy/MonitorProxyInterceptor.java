package com.stuben.monitop.client.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.context.ApplicationContext;

import com.stuben.monitop.client.common.MonitorClient;
import com.stuben.monitop.client.common.MonitorClientWrapper;
import com.stuben.monitop.client.common.MonitorContext;
import com.stuben.monitop.common.MonitorMsg;

public class MonitorProxyInterceptor implements AfterReturningAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorProxyInterceptor.class);

    private ApplicationContext applicationContext;
    private Integer pileNo;

    MonitorProxyInterceptor(ApplicationContext applicationContext, MonitorProxyInfo proxyInfo) {
        this.applicationContext = applicationContext;
        this.pileNo = proxyInfo.getPileNo();
        MonitorContext.put(proxyInfo);
    }


    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
        MonitorProxyUtils.exec(() -> {
            try {
                MonitorClient monitorClient = MonitorClientWrapper.get(applicationContext);
                if (null == monitorClient) {
                    LOGGER.warn("null monitor client , pileNo:{}", pileNo);
                    return;
                }

                Integer num = MonitorContext.getNum(pileNo, args);
                if (null != num) {
                    monitorClient.report(new MonitorMsg(pileNo, num));
                }

            } catch (Exception e) {
                LOGGER.warn("MonitorProxy error , pileNo:{}", pileNo, e);
            }
        });
    }


}
