package com.stuben.monitop.flink;

import java.util.List;

import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.common.MonitorMsg;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;

public class TestRocketMqConsumer {
    public static void main(String[] args) throws MQClientException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("mq_test_consumer");

        // Specify name server addresses.
        consumer.setNamesrvAddr("10.51.7.34:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe(MonitorConstant.MONITOR_SINK_TOPIC, "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                msgs.forEach(messageExt -> System.out.println(JSON.parseObject(messageExt.getBody(), MonitorMsg.class).toString()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
