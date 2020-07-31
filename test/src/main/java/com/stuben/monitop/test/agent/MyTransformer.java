package com.stuben.monitop.test.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class MyTransformer implements ClassFileTransformer {

    /**
     * 每次修改 ,
     *
     * 为enable , 都要记录一下哪个类修改 , MonitorProxyInfo的内容
     *
     * 为disable , 移除记录的MonitorProxyInfo , 然后判断其他的MonitorProxyInfo是否存在 , 存在的话要重新刷新一遍
     */

    private static ClassPool CLASS_POOL = ClassPool.getDefault();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {

        try {
            System.out.println("transform start");

            String clzName = className.replace("/", ".");

            CtClass ctClass = CLASS_POOL.get(clzName);

            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }

            if (ctClass.isModified()) {
                return null;
            } else {
                CtMethod test1 = ctClass.getDeclaredMethod("test1");
                test1.insertBefore("com.stuben.monitop.test.service.MonitorTest.m($args);");
            }

            System.out.println("transform success");

            return ctClass.toBytecode();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalClassFormatException(e.getMessage());
        }
    }
}
