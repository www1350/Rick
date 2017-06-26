package com.absurd.rick;

import com.absurd.rick.service.CarService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by wangwenwei on 17/6/26.
 */
public class LockTest extends SpringMvcTest{
    @Autowired
    private CarService carService;

    @Test
    public void redisLock(){
        carService.priceUpdate("c5aY1btjdx",100D);

    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisLockSim(){
        Boolean flag = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize("abcdefgere");
                return connection.setNX(data,data);
            }
        });


        System.out.println(flag);

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize("abcdefgere");
                return connection.del(data);
            }
        });

        flag = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize("abcdefgere");
                return connection.setNX(data,data);
            }
        });


        System.out.println(flag);
    }

    @Test
    public void del(){

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize("abcdefgere");
                return connection.del(data);
            }
        });

        StringRedisSerializer serializer = new StringRedisSerializer();
        byte[] data = serializer.serialize("abcdefgere");
        String value = (String) redisTemplate.opsForValue().get(data);
        System.out.println(value);
    }
}
