package com.stuben.monitop.server.facade;

import java.util.List;

import com.stuben.monitop.client.common.IMonitorProxyFacade;
import com.stuben.monitop.client.proxy.MonitorProxyInfoResult;
import com.stuben.monitop.server.service.MonitorProxyService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MonitorProxyFacadeImpl implements IMonitorProxyFacade {
    @Autowired
    private MonitorProxyService monitorProxyService;

    @Override
    public List<MonitorProxyInfoResult> listProxys(String appName) {
        return monitorProxyService.listProxys(appName);
    }
}
