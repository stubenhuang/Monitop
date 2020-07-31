package com.stuben.monitop.client.premain;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stuben.monitop.client.common.MonitorContext;
import com.stuben.monitop.client.proxy.MonitorProxyInfo;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavassistTransformer implements ClassFileTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavassistTransformer.class);
    private static final ClassPool CLASS_POOL = ClassPool.getDefault();
    private MonitorProxyInfo newInfo;

    public JavassistTransformer(MonitorProxyInfo newInfo) {
        this.newInfo = newInfo;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            if (newInfo.getEnable()) {
                MonitorProxyInfo oldInfo = MonitorContext.put(newInfo);
                if (null == oldInfo) {
                    addInterceptor(newInfo);
                } else if (!oldInfo.equals(newInfo)) {
                    refreshAllMeta(oldInfo);// TODO notify old to refresh old class
                }
            } else {
                MonitorContext.remove(newInfo);
                refreshAllMeta(newInfo);
            }

            return CLASS_POOL.get(newInfo.getClz().getName()).toBytecode();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addInterceptor(MonitorProxyInfo info) throws NotFoundException, CannotCompileException {
        CtClass ctClass = CLASS_POOL.get(info.getClz().getName());
        CtMethod ctMethod = ctClass.getDeclaredMethod(info.getMethodName());
        if (null == ctMethod) {
            LOGGER.error("Can not find monitor meta , proxy info:{}", info);
            return;
        }
        ctMethod.insertAfter(String.format("com.stuben.monitop.client.premain.AgentMonitorInterceptor.report(%d,$args)", info.getPileNo()));
    }

    private void refreshAllMeta(MonitorProxyInfo info) throws NotFoundException, CannotCompileException {
        CLASS_POOL.get(info.getClz().getName()).detach();

        List<MonitorProxyInfo> monitorProxyInfos = MonitorContext.listProxyInfoByClz(info.getClz());
        for (MonitorProxyInfo refreshInfo : monitorProxyInfos) {
            addInterceptor(refreshInfo);
        }
    }
}
