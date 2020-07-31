package com.stuben.monitop.client.proxy;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;


public class MonitorProxyCreator extends AbstractAutoProxyCreator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> suffixs;
    private Set<String> excludes;

    public MonitorProxyCreator(List<String> suffixs, Set<String> excludes) {
        this.suffixs = suffixs;
        this.excludes = excludes;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        if (CollectionUtils.isEmpty(suffixs)) {
            return DO_NOT_PROXY;
        }

        try {
            if (beanClass.isAssignableFrom(Advised.class)) {
                return DO_NOT_PROXY;
            }

            if (excludes.contains(beanName)) {
                return DO_NOT_PROXY;
            }

            String beanClassName = beanClass.getName();
            if (Proxy.isProxyClass(beanClass)) {
                beanClassName = beanClass.getInterfaces()[0].getName();
            }

            for (String suffix : suffixs) {
                if (beanClassName.endsWith(suffix)) {
                    logger.debug("getAdvicesAndAdvisorsForBean , beanClass:{} , beanName:{}  ", beanClass, beanName);
                    return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
                }
            }
        } catch (Exception e) {
            logger.error("getAdvicesAndAdvisorsForBean failed", e);
        }

        return DO_NOT_PROXY;
    }

    @Override
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        return true;
    }
}
