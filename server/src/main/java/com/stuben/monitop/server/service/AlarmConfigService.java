package com.stuben.monitop.server.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.stuben.monitop.server.dao.mysql.AlarmConfigMapper;
import com.stuben.monitop.server.dao.po.AlarmConfigPO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;

@Service
public class AlarmConfigService {

    @Autowired
    private AlarmConfigMapper alarmConfigMapper;

    /**
     * 获取告警配置
     * 
     * @param app
     * @param pileNo
     * @return
     */
    public List<AlarmConfigPO> getAlarmConfig(String app, int pileNo) {

        Wrapper<AlarmConfigPO> wrapper = Condition.<AlarmConfigPO>wrapper().eq("app", app).eq("pileNo", pileNo).eq("enable", true);

        return alarmConfigMapper.selectList(wrapper);
    }

    /**
     * 获取一些长时间没有心跳的配置
     * 
     * @return
     */
    public List<AlarmConfigPO> someNoHeartbeatConfig() {

        Wrapper<AlarmConfigPO> wrapper = Condition.<AlarmConfigPO>wrapper().lt("lastHeartbeatTime", DateUtils.addSeconds(new Date(), -10)).eq("enable", true);

        List<AlarmConfigPO> alarmConfigPOS = alarmConfigMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(alarmConfigPOS)) {
            return Collections.emptyList();
        }

        Collections.shuffle(alarmConfigPOS);

        return alarmConfigPOS.subList(0, alarmConfigPOS.size() / 10 + 1);
    }

    /**
     * 更新配置的心跳时间
     * 
     * @param id
     */
    public void updateHeartbeat(Integer configId) {
        AlarmConfigPO alarmConfigPO = new AlarmConfigPO();
        alarmConfigPO.setId(configId);
        alarmConfigPO.setLastHeartbeatTime(new Date());
        alarmConfigMapper.updateById(alarmConfigPO);
    }
}
