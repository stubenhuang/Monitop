package com.stuben.monitop.client.premain;

import com.stuben.monitop.client.common.InstrumentationSingleton;
import com.stuben.monitop.client.common.UseAgentCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(UseAgentCondition.class)
public class AgentMonitorAutoConfiguration {

    @Bean
    public AgentMonitorCreator agentMonitorCreator() {
        return new AgentMonitorCreator(InstrumentationSingleton.get());
    }

}
