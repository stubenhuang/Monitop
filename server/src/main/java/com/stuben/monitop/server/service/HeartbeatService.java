package com.stuben.monitop.server.service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.common.MonitorMsg;
import com.stuben.monitop.server.dao.po.AlarmConfigPO;


@Service
public class HeartbeatService {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostConstruct
    public void heartbeat() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            List<AlarmConfigPO> alarmConfigPOS = alarmConfigService.someNoHeartbeatConfig();
            if (CollectionUtils.isEmpty(alarmConfigPOS)) {
                return;
            }

            for (AlarmConfigPO alarmConfigPO : alarmConfigPOS) {
                alarmConfigService.updateHeartbeat(alarmConfigPO.getId());

                MonitorMsg monitorMsg = new MonitorMsg();
                monitorMsg.setApp(alarmConfigPO.getApp());
                monitorMsg.setPileNo(alarmConfigPO.getPileNo());
                monitorMsg.setNum(0);

                rocketMQTemplate.convertAndSend(MonitorConstant.SOURCE_TOPIC, monitorMsg);
            }

        }, 3, 1, TimeUnit.SECONDS);
    }

}
