package com.absurd.rick.base;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


/**
 * @author Absurd.
 */
@Slf4j
@Component
public class CacheService {
    @Autowired
    private RedisTemplate redisTemplate;
    public Object getList(String key, Class<?> clazz) {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String jsonStr = (String) opsForValue.get(key);
        log.info("get:"+jsonStr);
        return StringUtils.isEmpty(jsonStr) ? Lists.newArrayList() : JSON.parseArray(jsonStr,clazz);

    }


    public Object getObject(String key, Class<?> clazz) {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String jsonStr = (String) opsForValue.get(key);
        log.info("get:"+jsonStr);
        return StringUtils.isEmpty(jsonStr) ? null :  JSON.parseObject(jsonStr, clazz);
    }


    public void setObject(String key, Object result, int time, TimeUnit seconds) {
        String jsonStr = JSON.toJSONString(result);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key,jsonStr,time,seconds);
    }
}
