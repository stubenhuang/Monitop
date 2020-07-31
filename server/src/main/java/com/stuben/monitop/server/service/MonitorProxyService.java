package com.stuben.monitop.server.service;

import java.util.List;
import java.util.stream.Collectors;

import com.stuben.monitop.client.proxy.MonitorProxyInfoResult;
import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.server.dao.mysql.MonitorProxyInfoMapper;
import com.stuben.monitop.server.dao.po.MonitorProxyInfoPO;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Condition;

@Service
public class MonitorProxyService {

    @Autowired
    private MonitorProxyInfoMapper monitorProxyInfoMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 列取监控配置
     * 
     * @param appName 应用名称
     * @return
     */
    public List<MonitorProxyInfoResult> listProxys(String appName) {

        List<MonitorProxyInfoPO> monitorProxyInfoPOS =
                monitorProxyInfoMapper.selectList(Condition.<MonitorProxyInfoPO>wrapper().eq("appName", appName).eq("enable", true).ne("classFullName", ""));

        return monitorProxyInfoPOS.stream().map(this::poToResult).collect(Collectors.toList());
    }



    /**
     * 刷新配置
     * 
     * @param id 配置ID
     * @return
     */
    public boolean refresh(Integer id) {
        MonitorProxyInfoPO monitorProxyInfoPO = monitorProxyInfoMapper.selectById(id);
        if (null == monitorProxyInfoPO || StringUtils.isBlank(monitorProxyInfoPO.getClassFullName())) {
            return false;
        }

        rocketMQTemplate.convertAndSend(MonitorConstant.PROXY_INFO_REFRESH + ":" + monitorProxyInfoPO.getAppName(), poToResult(monitorProxyInfoPO));

        return true;
    }

    /**
     * 把po改成result
     * 
     * @param po
     * @return
     */
    private MonitorProxyInfoResult poToResult(MonitorProxyInfoPO po) {
        MonitorProxyInfoResult result = new MonitorProxyInfoResult();
        result.setPileNo(po.getPileNo());
        result.setClassFullName(po.getClassFullName());
        result.setMethodName(po.getMethodName());
        result.setParam(po.getParam());
        result.setCondition(po.getCondition());
        result.setEnable(po.getEnable());
        return result;
    }
}
