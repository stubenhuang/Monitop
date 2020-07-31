package com.stuben.monitop.test.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class Test {

    private static MyTransformer transformer = new MyTransformer();

    public void test1(String content, String content2) {
        System.out.println("Hello " + content + "," + content2);
    }

    // public void test2() {
    // System.out.println("Hello test2()");
    // }

    public static void premain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        inst.addTransformer(transformer, true);
        inst.retransformClasses(Test.class);
        inst.removeTransformer(transformer);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            inst.addTransformer(transformer, true);
            try {
                inst.retransformClasses(Test.class);
            } catch (UnmodifiableClassException e) {
                e.printStackTrace();
            }
            inst.removeTransformer(transformer);

        }).start();
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.test1("11111", "2222222");
        // test.test2();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        test.test1("3333333", "444444");
        // test.test2();
    }
}
