package com.absurd.rick.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangwenwei on 17/6/16.
 */
@Slf4j
@Component(value = "redisCache")
public class RedisCacheImpl implements CacheService{
    @Autowired
    private RedisTemplate redisTemplate;
    public <T> List<T> getList(String key, Class<T> clazz) {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String jsonStr = (String) opsForValue.get(key);
        log.info("get:"+jsonStr);
        return StringUtils.isEmpty(jsonStr) ? Lists.newArrayList() : JSON.parseArray(jsonStr,clazz);
    }


    public <T> T getObject(String key, Class<T> clazz) {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String jsonStr = (String) opsForValue.get(key);
        log.info("get:"+jsonStr);
        return StringUtils.isEmpty(jsonStr) ? null :  JSON.parseObject(jsonStr, clazz);
    }


    public <T> void setObject(String key, T result, int time, TimeUnit seconds) {
        String jsonStr = JSON.toJSONString(result);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key,jsonStr,time,seconds);
    }

    @Override
    public void touch(String key, int time, TimeUnit seconds) {
        redisTemplate.expire(key,time,seconds);
    }

    @Override
    public void del(String key) {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key,null);
    }
}
