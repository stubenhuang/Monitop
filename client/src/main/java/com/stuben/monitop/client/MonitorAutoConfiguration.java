package com.stuben.monitop.client;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.stuben.monitop.client.common.MonitorClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stuben.monitop.client.proxy.MonitorProxyCreator;
import com.stuben.monitop.client.proxy.MonitorProxyRefresher;
import com.stuben.monitop.client.proxy.MonitorProxyStartup;

@Configuration
public class MonitorAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name}")
    private String appName;
    @Value("${monitop.suffixs:}")
    private String othorSuffixs;
    @Value("${monitop.excludes:}")
    private String excludes;

    @Bean
    public MonitorProxyCreator monitorProxyProxyCreator() {

        List<String> suffixs = Lists.newArrayList("Action", "Mapper", "ServiceImpl", "FacadeImpl", "Async");

        if (StringUtils.isNotBlank(othorSuffixs)) {
            suffixs.addAll(Arrays.asList(othorSuffixs.split(",")));
        }

        Set<String> excludes = Sets.newHashSet("scriptConsumer", "jacksonObjectMapper");
        if (StringUtils.isNotBlank(this.excludes)) {
            excludes.addAll(Arrays.asList(this.excludes.split(",")));
        }

        return new MonitorProxyCreator(suffixs, excludes);
    }


    @Bean
    public MonitorProxyRefresher monitorProxyRefresher(ApplicationContext applicationContext) {
        return new MonitorProxyRefresher(applicationContext);
    }

    @Bean
    public MonitorProxyStartup monitorProxyStartUp(MonitorProxyRefresher monitorProxyRefresher) {
        return new MonitorProxyStartup(monitorProxyRefresher, appName);
    }

    @Bean
    public MonitorClient monitorClient() {
        logger.info("init monitorClient , appname:{} ", appName);
        return new MonitorClient(appName);
    }

}
