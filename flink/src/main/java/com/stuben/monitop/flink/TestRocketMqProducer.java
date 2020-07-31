package com.stuben.monitop.flink;

import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import com.alibaba.fastjson.JSON;
import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.common.MonitorMsg;

public class TestRocketMqProducer {
    public static void main(String[] args) {
        DefaultMQProducer producer = null;
        try {
            // Instantiate with a producer group name.
            producer = new DefaultMQProducer("mq_test_produce");
            // Specify name server addresses.
            producer.setNamesrvAddr("10.51.7.34:9876");
            // Launch the instance.
            producer.start();
            for (int i = 0; i < 100000; i++) {
                MonitorMsg monitorMsg = new MonitorMsg(1, RandomUtils.nextInt(1, 10));
                monitorMsg.setApp("test" + RandomUtils.nextInt(1, 5));

                // Create a message instance, specifying topic, tag and message body.
                Message msg = new Message(MonitorConstant.SOURCE_TOPIC, JSON.toJSONBytes(monitorMsg));

                // Call send message to deliver message to one of brokers.
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", monitorMsg);

                Thread.sleep(RandomUtils.nextInt(1000, 5000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Shut down once the producer instance is not longer in use.
            if (null != producer) {
                producer.shutdown();
            }
        }
    }
}
