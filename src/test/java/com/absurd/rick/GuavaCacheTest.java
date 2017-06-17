package com.absurd.rick;

import com.absurd.rick.cache.CacheService;
import com.absurd.rick.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangwenwei on 17/6/17.
 */
@Slf4j
public class GuavaCacheTest extends SpringMvcTest {
    @Resource(name = "guavaCache")
    private CacheService cacheService;

    private static final ExecutorService executors = Executors.newFixedThreadPool( 2 * Runtime.getRuntime().availableProcessors());

    @Test
    public void getAndSet() throws InterruptedException {
        cacheService.setObject("aaa",new User("www","www","www","www",new Date(),new Date(),"www"),100, TimeUnit.SECONDS);
        executors.submit(()->{
            for (int i=0;i<100;i++) {
                User user = cacheService.getObject("aaa", User.class);
                log.info(Thread.currentThread().getName() + user.toString());
            }
        });
        executors.submit(()->{
            for (int i=0;i<100;i++) {
                User user = cacheService.getObject("aaa", User.class);
                log.info(Thread.currentThread().getName() + user.toString());
            }
        });
        Thread.sleep(3000L);
    }
}
