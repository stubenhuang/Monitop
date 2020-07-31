package com.stuben.monitop.client.common;

import com.stuben.monitop.client.proxy.MonitorProxyInfo;
import com.stuben.monitop.client.proxy.MonitorProxyInfoResult;

public class MonitorUtils {

    /**
     * 从result结果中生成info
     *
     * @param result
     * @return
     */
    public static MonitorProxyInfo convertFromResult(MonitorProxyInfoResult result) throws ClassNotFoundException {
        Class<?> clz = Class.forName(result.getClassFullName());
        MonitorProxyInfo monitorProxyInfo = new MonitorProxyInfo();
        monitorProxyInfo.setClz(clz);
        monitorProxyInfo.setPileNo(result.getPileNo());
        monitorProxyInfo.setMethodName(result.getMethodName());
        monitorProxyInfo.setParam(result.getParam());
        monitorProxyInfo.setCondition(result.getCondition());
        monitorProxyInfo.setEnable(result.getEnable());
        return monitorProxyInfo;
    }
}
