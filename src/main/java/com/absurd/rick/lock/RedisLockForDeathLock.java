package com.absurd.rick.lock;

import com.absurd.rick.util.ByteUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by wangwenwei on 17/6/30.
 */
public class RedisLockForDeathLock implements Lock{

    private final RedisTemplate redisTemplate;

    private final String lockName;

    private final String KEY_PREIX = "redis-death-lock:";

    /***
     * 获取不到锁的休眠时间
     */
    private final long sleepTime = 100L;

    /***
     * 持有锁的最长时间
     */
    private final long expireTime = 300L;


    /***
     * 锁中断状态
     */
    private boolean interruped = true;


    public RedisLockForDeathLock(RedisTemplate redisTemplate, String lockName) {
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
            if (setNX()){
                break;
            } else{
                long oldExpireTime = getOldExpireTime();
                if (oldExpireTime < System.currentTimeMillis()){
                    if (getAndSet()) break;
                }else{
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean getAndSet() {
        Boolean flag = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
             @Override
             public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                 StringRedisSerializer serializer = new StringRedisSerializer();
                 byte[] data = serializer.serialize(lockName);
                 long newExpireTime = System.currentTimeMillis() + expireTime;
                 byte[] rtExpireTime= connection.getSet(data, ByteUtils.LongTobyte(newExpireTime));
                 Long rtExpireTimeL = ByteUtils.byteToLong(rtExpireTime);
                 if (rtExpireTimeL == null) return false;
                 if (newExpireTime == rtExpireTimeL.longValue() ){
                     return true;
                 }else
                     return false;
             }
         });
        return flag.booleanValue();
    }

    private boolean setNX() {
        Boolean flag =  (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize(lockName);
                long currentTime = System.currentTimeMillis();
                return connection.setNX(data, ByteUtils.LongTobyte(currentTime+expireTime));
            }
        });
        if (flag == null) return false;
        return flag.booleanValue();
    }

    private long getOldExpireTime() {
        Long old = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                byte[] data = serializer.serialize(lockName);
                byte[] value = connection.get(data);
                return ByteUtils.byteToLong(value);
            }
        });
        if (old == null)
            return 0;
        return old.longValue();
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
            throw new NullPointerException("key is null");
        if (setNX()){
            return true;
        } else{
            long oldExpireTime = getOldExpireTime();
            if (oldExpireTime < System.currentTimeMillis() && getAndSet()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (redisTemplate == null)
            throw new NullPointerException("jedis is null");
        if (lockName == null)
            throw new NullPointerException("key is null");
        long now = System.currentTimeMillis();
        long timeOutAt = now + calcSeconds(time, unit);
        while (true){
            if (!interruped)
                throw new RuntimeException("获取锁状态被中断");
            if (setNX()){
                return true;
            } else{
                // 获取锁超时
                if (System.currentTimeMillis() > timeOutAt)
                    return false;
                long oldExpireTime = getOldExpireTime();
                if (oldExpireTime < System.currentTimeMillis()){
                    if (getAndSet()) return true;
                }else{
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        try {
            long oldExpireTime = getOldExpireTime();
            if (oldExpireTime > System.currentTimeMillis()){
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = serializer.serialize(lockName);
                    return connection.del(data);
                }
            });
        }
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
