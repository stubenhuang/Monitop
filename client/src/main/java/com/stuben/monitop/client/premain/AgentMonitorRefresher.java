package com.stuben.monitop.client.premain;

import com.stuben.monitop.client.common.MonitorUtils;
import com.stuben.monitop.client.proxy.MonitorProxyAdvisor;
import com.stuben.monitop.client.proxy.MonitorProxyInfo;
import com.stuben.monitop.client.proxy.MonitorProxyInfoResult;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.Objects;

public class AgentMonitorRefresher {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Instrumentation instrumentation;


    public AgentMonitorRefresher(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    synchronized void refreshMonitor(MonitorProxyInfoResult result) {
        try {
            doRefresh(result);
        } catch (Exception e) {
            logger.error("refreshMonitor error", e);
        }
    }

    private void doRefresh(MonitorProxyInfoResult result) throws UnmodifiableClassException, ClassNotFoundException {
        if (null == result) {
            logger.error("null result");
            return;
        }

        MonitorProxyInfo proxyInfo = MonitorUtils.convertFromResult(result);
        try {
            JavassistTransformer javassistTransformer = new JavassistTransformer(proxyInfo);
            instrumentation.addTransformer(javassistTransformer, true);
            instrumentation.retransformClasses(proxyInfo.getClz());
            instrumentation.removeTransformer(javassistTransformer);
        } catch (Exception e) {
            logger.error("Agent Error");
        }
        this.instrumentation.retransformClasses(proxyInfo.getClz());
    }

}
