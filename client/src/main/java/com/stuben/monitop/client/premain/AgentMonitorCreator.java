package com.stuben.monitop.client.premain;


import java.lang.instrument.Instrumentation;

public class AgentMonitorCreator {

    private Instrumentation instrumentation;

    public AgentMonitorCreator(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
}
