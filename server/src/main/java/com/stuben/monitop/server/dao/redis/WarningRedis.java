package com.stuben.monitop.server.dao.redis;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 预警存储
 */
@Component
public class WarningRedis {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    private String key(Integer alarmConfigId) {
        return "monitop:warning:" + alarmConfigId;
    }

    /**
     * 列表
     * 
     * @param alarmConfigId
     * @param start
     * @param end
     * @return
     */
    public List<Integer> lrange(Integer alarmConfigId, Integer start, Integer end) {
        return redisTemplate.opsForList().range(key(alarmConfigId), start, end);
    }

    /**
     * 推入
     * 
     * @param alarmConfigId
     * @param monitorNum
     */
    public void lpush(Integer alarmConfigId, Integer monitorNum) {
        redisTemplate.opsForList().leftPush(key(alarmConfigId), monitorNum);
    }

    /**
     * 长度
     * 
     * @param alarmConfigId
     * @return
     */
    public long llen(Integer alarmConfigId) {
        return redisTemplate.opsForList().size(key(alarmConfigId));
    }

    /**
     * 删除
     * 
     * @param alarmConfigId
     */
    public void del(Integer alarmConfigId) {
        redisTemplate.opsForList().getOperations().delete(key(alarmConfigId));
    }
}
