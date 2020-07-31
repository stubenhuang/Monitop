package com.stuben.monitop.server.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.stuben.monitop.common.MonitorMsg;
import com.stuben.monitop.server.dao.elasticsearch.EsDao;
import com.stuben.monitop.server.dao.po.AlarmConfigPO;
import com.stuben.monitop.server.dao.po.MonitorDataPO;
import com.stuben.monitop.server.service.strategy.AbstractStrategy;
import com.stuben.monitop.server.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AlarmService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<Integer, AbstractStrategy> strategyMap = new HashMap<>();

    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EsDao<MonitorDataPO> esDao;

    public void checkMsg(MonitorMsg monitorMsg) {

        // 保存数据
        esDao.saveOrUpdate(msgToPo(monitorMsg));

        // 检查是否触发预警
        List<AlarmConfigPO> alarmConfigs = alarmConfigService.getAlarmConfig(monitorMsg.getApp(), monitorMsg.getPileNo());
        for (AlarmConfigPO alarmConfig : alarmConfigs) {
            AbstractStrategy strategy = strategyMap.get(alarmConfig.getWarningStrategy());
            boolean isAlarm = strategy.isAlarm(alarmConfig, monitorMsg);
            if (isAlarm) {
                List<Integer> records = strategy.getAndClearWarningRecord(alarmConfig.getId());
                emailService.sendAlarmMail(alarmConfig, records);
                logger.info("alarm , alarmConfig id : {} , records:{}", alarmConfig.getId(), records);
            }
        }
    }

    @Autowired
    public void setStrategyMap(List<AbstractStrategy> strategyList) {
        this.strategyMap = strategyList.stream().collect(Collectors.toMap(s -> s.type().getId(), s -> s));
    }

    private MonitorDataPO msgToPo(MonitorMsg monitorMsg) {
        MonitorDataPO po = new MonitorDataPO();
        po.setId(CommonUtils.getMongoId(monitorMsg.getApp(), monitorMsg.getPileNo(), monitorMsg.getTimestamp()));
        po.setApp(monitorMsg.getApp());
        po.setPileNo(monitorMsg.getPileNo());
        po.setMinuteTime(new Date(monitorMsg.getTimestamp()));
        po.setNum(monitorMsg.getNum());
        return po;
    }
}
