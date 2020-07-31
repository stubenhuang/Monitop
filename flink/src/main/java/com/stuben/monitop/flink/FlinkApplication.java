package com.stuben.monitop.flink;

import com.alibaba.fastjson.JSON;
import com.stuben.monitop.common.MonitorAggregate;
import com.stuben.monitop.common.MonitorConstant;
import com.stuben.monitop.common.MonitorMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.rocketmq.flink.RocketMQConfig;
import org.apache.rocketmq.flink.RocketMQSink;
import org.apache.rocketmq.flink.RocketMQSource;
import org.apache.rocketmq.flink.common.selector.DefaultTopicSelector;
import org.apache.rocketmq.flink.common.serialization.KeyValueSerializationSchema;
import org.apache.rocketmq.flink.common.serialization.SimpleKeyValueDeserializationSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class FlinkApplication {

    private static final boolean isDebug = false;

    /**
     * 打包方式 , 注意isDebug要为false
     *
     * 测试环境: mvn clean package -P dev
     *
     * 线上环境: mvn clean package -P online
     */
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(isDebug ? TimeCharacteristic.ProcessingTime : TimeCharacteristic.EventTime);
        env.enableCheckpointing(1000);
        env.setStateBackend(new FsStateBackend("file:///data/flink-1.8.1/backend"));// TODO 上线要找个分布式系统

        // 每分钟数据监控
        WindowAssigner<Object, TimeWindow> reduceWindow =
                isDebug ? TumblingProcessingTimeWindows.of(Time.seconds(3)) : TumblingEventTimeWindows.of(Time.minutes(1));
        SingleOutputStreamOperator<MonitorMsg> reduce = env.addSource(getSource()).map(getMapFunc())
                .assignTimestampsAndWatermarks(getAssignerWithPeriodicWatermarks()).keyBy("app", "pileNo").window(reduceWindow).reduce(doReduce());
        reduce.addSink(getMonitorSink());

        // 当天均值/最大值/最小值/数量/总值统计
        WindowAssigner<Object, TimeWindow> aggregateWindow =
                isDebug ? TumblingProcessingTimeWindows.of(Time.seconds(15)) : TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8));
        SingleOutputStreamOperator<MonitorAggregate> aggregate = reduce.keyBy("app", "pileNo").window(aggregateWindow).apply(doAggregate());
        aggregate.addSink(getAggregateSink());

        try {
            env.execute("rocketmq-flink-example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 每天的合计操作
     */
    private static WindowFunction<MonitorMsg, MonitorAggregate, Tuple, TimeWindow> doAggregate() {
        return new WindowFunction<MonitorMsg, MonitorAggregate, Tuple, TimeWindow>() {
            @Override
            public void apply(Tuple tuple, TimeWindow window, Iterable<MonitorMsg> input, Collector<MonitorAggregate> out) throws Exception {
                int sum = 0;
                int count = 0;
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for (MonitorMsg msg : input) {
                    sum += msg.getNum();
                    count++;

                    min = Math.min(min, msg.getNum());
                    max = Math.max(max, msg.getNum());
                }
                int average = sum / count;

                MonitorMsg monitorMsg = input.iterator().next();

                MonitorAggregate monitorAggregate = new MonitorAggregate();
                monitorAggregate.setApp(monitorMsg.getApp());
                monitorAggregate.setPileNo(monitorMsg.getPileNo());
                monitorAggregate.setDateTimestamp(getNowDayTimestamp(monitorMsg.getTimestamp()));
                monitorAggregate.setCount(count);
                monitorAggregate.setSum(sum);
                monitorAggregate.setAverage(average);
                monitorAggregate.setMax(max);
                monitorAggregate.setMin(min);

                out.collect(monitorAggregate);
            }
        };
    }

    /**
     * 每分钟的统计操作
     */
    private static ReduceFunction<MonitorMsg> doReduce() {
        return (m1, m2) -> {
            m1.setNum(m1.getNum() + m2.getNum());
            m1.setTimestamp(getNowMinTimestamp(m1.getTimestamp()));
            return m1;
        };
    }

    /**
     * 转化map成MonitorMsg
     */
    private static MapFunction<Map, MonitorMsg> getMapFunc() {
        return map -> JSON.parseObject(JSON.parseObject(map.get("value").toString()).toJSONString(), MonitorMsg.class);
    }

    /**
     * flink的水位线
     */
    private static AssignerWithPeriodicWatermarks<MonitorMsg> getAssignerWithPeriodicWatermarks() {
        return new BoundedOutOfOrdernessTimestampExtractor<MonitorMsg>(Time.seconds(10)) {
            @Override
            public long extractTimestamp(MonitorMsg element) {
                return element.getTimestamp();
            }
        };
    }

    /**
     * 监控输出
     *
     */
    private static SinkFunction<MonitorMsg> getMonitorSink() {
        if (isDebug) {
            return new PrintSinkFunction<>();
        }

        Properties producerProps = new Properties();
        producerProps.setProperty(RocketMQConfig.NAME_SERVER_ADDR, mqProperties.getProperty("nameservers"));
        producerProps.setProperty(RocketMQConfig.PRODUCER_GROUP, mqProperties.getProperty("group"));
        return new RocketMQSink<>(new Serialization<>(), new DefaultTopicSelector<>(MonitorConstant.MONITOR_SINK_TOPIC), producerProps);
    }

    /**
     * 统计输出
     *
     */
    private static SinkFunction<MonitorAggregate> getAggregateSink() {
        if (isDebug) {
            return new PrintSinkFunction<>();
        }

        Properties producerProps = new Properties();
        producerProps.setProperty(RocketMQConfig.NAME_SERVER_ADDR, mqProperties.getProperty("nameservers"));
        producerProps.setProperty(RocketMQConfig.PRODUCER_GROUP, mqProperties.getProperty("group"));
        return new RocketMQSink<>(new Serialization<>(), new DefaultTopicSelector<>(MonitorConstant.AGGREGATE_SINK_TOPIC), producerProps);
    }


    /**
     * 输入
     */
    private static SourceFunction<Map> getSource() {
        Properties consumerProps = new Properties();
        consumerProps.setProperty(RocketMQConfig.NAME_SERVER_ADDR, mqProperties.getProperty("nameservers"));
        consumerProps.setProperty(RocketMQConfig.CONSUMER_GROUP, mqProperties.getProperty("group") + (isDebug ? "test" : ""));
        consumerProps.setProperty(RocketMQConfig.CONSUMER_TOPIC, MonitorConstant.SOURCE_TOPIC);
        return new RocketMQSource<>(new SimpleKeyValueDeserializationSchema("mqTag", "value"), consumerProps);
    }

    /**
     * 序列化
     */
    static class Serialization<T> implements KeyValueSerializationSchema<T> {
        @Override
        public byte[] serializeKey(T msg) {
            return StringUtils.EMPTY.getBytes();
        }

        @Override
        public byte[] serializeValue(T msg) {
            return JSON.toJSONBytes(msg);
        }
    }

    /**
     * 获取当前分钟的时间戳
     * 
     */
    private static long getNowMinTimestamp(long timestamp) {
        return DateUtils.truncate(new Date(timestamp), Calendar.MINUTE).getTime();
    }

    /**
     * 获取当前天数的时间戳
     *
     */
    private static long getNowDayTimestamp(long timestamp) {
        return DateUtils.truncate(new Date(timestamp), Calendar.DATE).getTime();
    }

    /**
     * 读取配置
     */
    private static Properties mqProperties = new Properties();
    static {
        InputStream resourceAsStream = FlinkApplication.class.getClassLoader().getResourceAsStream("rocketmq.properties");
        try {
            mqProperties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
