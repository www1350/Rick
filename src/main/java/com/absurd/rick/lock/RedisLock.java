package com.absurd.rick.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by wangwenwei on 17/6/26.
 */
@Slf4j
public class RedisLock implements Lock{

    private final RedisTemplate redisTemplate;

    private final String lockName;

    private final String KEY_PREIX = "redis-lock:";


    /***
     * 获取不到锁的休眠时间
     */
    private final long sleepTime = 100L;

    /***
     * 持有锁的最长时间
     */
    private final long expireTime = 300L;

    /***
     * 超时时间
     */
    private long expireTimeOut = 0;


    /***
     * 锁中断状态
     */
    private boolean interruped = true;

    public RedisLock(RedisTemplate redisTemplate, String lockName) {
        this.redisTemplate = redisTemplate;
        this.lockName = KEY_PREIX + lockName;
    }

    @Override
    public void lock() {
        if (redisTemplate == null)
            throw new NullPointerException("jedis is null");
        if (lockName == null)
            throw new NullPointerException("key is null");
        while (true){
            if (!interruped)
                throw new RuntimeException("获取锁状态被中断");
            Boolean flag = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = serializer.serialize(lockName);
                    return connection.setNX(data,data);
                }
            });
            if (flag == null || !flag.booleanValue()){
                try {
                    Thread.currentThread().sleep(this.sleepTime);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }else{
                expireTimeOut = System.currentTimeMillis() + expireTime;
                redisTemplate.expire(this.lockName,expireTime,TimeUnit.MILLISECONDS);
                break;
            }
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.interruped = false;
    }

    @Override
    public boolean tryLock() {
        if (redisTemplate == null)
            throw new NullPointerException("jedis is null");
        if (lockName == null)
            throw new NullPointerException("lockName is null");
        if (!interruped)
            throw  new RuntimeException("线程被中断");
        Boolean flag = (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize(lockName);
                return connection.setNX(data,data);
            }
        });
        if (flag == null || !flag)
            return false;
        else {
            // 设置锁过期时间
            expireTimeOut = System.currentTimeMillis() + expireTime;
            redisTemplate.expire(this.lockName, expireTime,TimeUnit.MILLISECONDS);
            return true;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (redisTemplate == null)
            throw new NullPointerException("jedis is null");
        if (lockName == null)
            throw new NullPointerException("lockName is null");
        if (time == 0)
            return false;
        long now = System.currentTimeMillis();
        long timeOutAt = now + calcSeconds(time, unit);
        while (true) {
            if (!interruped)
                throw new InterruptedException("线程被中断");
            Boolean flag = (Boolean) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = serializer.serialize(lockName);
                    return connection.setNX(data,data);
                }
            });
            // 加锁失败
            if (flag == null || !flag) {
                // 获取锁超时
                if (System.currentTimeMillis() > timeOutAt)
                    return false;
                try {
                    // 休眠一段时间，继续获取锁
                    Thread.currentThread().sleep(this.sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //获取锁成功，设置锁过期时间
                expireTimeOut = System.currentTimeMillis()  + expireTime;
                redisTemplate.expire(this.lockName, expireTime,TimeUnit.MILLISECONDS);
                return true;
            }

            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            //当前时间小于过期时间,则锁未超时,删除锁定
            if (expireTimeOut == 0 || System.currentTimeMillis()  < expireTimeOut)
                redisTemplate.execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisSerializer serializer = new StringRedisSerializer();
                        byte[] data = serializer.serialize(lockName);
                        return connection.del(data);
                    }
                });
        }catch (Exception e){

        }

    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("不支持当前的操作");
    }

    /**
     * 时间转换成毫秒
     * @param time
     * @param unit
     * @return
     */
    private long calcSeconds(long time, TimeUnit unit){
        if (unit == TimeUnit.DAYS)
            return time * 24 * 60 * 60 * 1000;
        else if (unit == TimeUnit.HOURS)
            return time * 60 * 60 * 1000;
        else  if (unit == TimeUnit.MINUTES)
            return time * 60 * 1000;
        else
            return time * 1000;
    }
}
