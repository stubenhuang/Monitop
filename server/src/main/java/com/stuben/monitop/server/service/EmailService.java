package com.stuben.monitop.server.service;

import java.util.List;

import com.stuben.monitop.server.dao.po.AlarmConfigPO;
import com.stuben.monitop.server.enums.StrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public JavaMailSender emailSender;


    private static final String DASHBOARD_URL_TEMPLATE =
            "http://10.1.5.53:3001/d/n5HnHldWz/jian-kong?orgId=1&from=now/d&to=now&var-var_app=%s&var-var_pileNo=%d";
    private static final String TITLE_TEMPLATE = "[趣约会监控] app:%s , pileNo:%d , name:%s";
    private static final String CONTENT_TEMPLATE = "监控策略 : %s \n\n"//
            + "阈值 : %d\n\n"//
            + "触发值 : %s\n\n"//
            + "监控面板 : %s";

    /**
     * 发送告警短信
     */
    public void sendAlarmMail(AlarmConfigPO alarmConfig, List<Integer> records) {

        String[] toEmails = alarmConfig.getAlarmEmails().split(",");
        String dashboardUrl = String.format(DASHBOARD_URL_TEMPLATE, alarmConfig.getApp(), alarmConfig.getPileNo());
        String title = String.format(TITLE_TEMPLATE, alarmConfig.getApp(), alarmConfig.getPileNo(), alarmConfig.getName());
        String content = String.format(CONTENT_TEMPLATE, StrategyEnum.of(alarmConfig.getWarningStrategy()).getMean(), alarmConfig.getWarningThreshold(),
                records.toString(), dashboardUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEmails);
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message);
    }
}
