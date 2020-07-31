package com.stuben.monitop.client.common;

import java.lang.instrument.Instrumentation;

public class InstrumentationSingleton {

    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static Instrumentation get() {
        return instrumentation;
    }
}
