package com.stuben.monitop.client.common;

import com.stuben.monitop.client.proxy.MonitorProxyInfoResult;

import java.util.List;

public interface IMonitorProxyFacade {

    /**
     * 获取app的桩点
     * 
     * @param appName
     * @return
     */
    List<MonitorProxyInfoResult> listProxys(String appName);
}
