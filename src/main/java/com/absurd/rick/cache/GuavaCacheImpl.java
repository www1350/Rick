package com.absurd.rick.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangwenwei on 17/6/16.
 */
@Slf4j
@Component(value = "guavaCache")
public class GuavaCacheImpl implements CacheService{
    private final static Map<String,LoadingCache<String,String>> CACHE_MAP = Maps.newConcurrentMap();

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        LoadingCache<String,String> loadingCache = CACHE_MAP.get(key);
        String jsonStr = null;
        if (loadingCache!=null){
            try {
                jsonStr = loadingCache.get(key);
            } catch (ExecutionException e) {
                log.error("error get {}",key,e);
            }
        }
        return StringUtils.isEmpty(jsonStr) ? Lists.newArrayList() : JSON.parseArray(jsonStr,clazz);

    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        LoadingCache<String,String> loadingCache = CACHE_MAP.get(key);
        String jsonStr = null;
        if (loadingCache!=null){
            try {
                jsonStr = loadingCache.get(key);
            } catch (ExecutionException e) {
                log.error("error get {}",key,e);
            }
        }
        return StringUtils.isEmpty(jsonStr) ? null :  JSON.parseObject(jsonStr, clazz);
    }

    @Override
    public <T> void setObject(String key, T result, int time, TimeUnit seconds) {
        String jsonStr = JSON.toJSONString(result);
        LoadingCache<String, String> loadingCache = CACHE_MAP.get(key);
        if (loadingCache == null){
            loadingCache = CacheBuilder.newBuilder()
                    .expireAfterAccess(time,seconds)
                    .build(new CacheLoader<String, String>() {
                        @Override
                        public String load(String key) throws Exception {
                            return null;
                        }
                    });
            CACHE_MAP.put(key,loadingCache);
        }
        loadingCache.put(key,jsonStr);

    }

    @Override
    public void touch(String key, int time, TimeUnit seconds) {
        LoadingCache<String, String> loadingCache = CACHE_MAP.get(key);
        String jsonStr = null;
        if ( loadingCache!=null ){
            try {
                jsonStr = loadingCache.get(key);
                loadingCache = CacheBuilder.newBuilder()
                        .expireAfterAccess(time,seconds)
                        .build(new CacheLoader<String, String>() {
                            @Override
                            public String load(String key) throws Exception {
                                return null;
                            }
                        });
                loadingCache.put(key,jsonStr);
                CACHE_MAP.put(key,loadingCache);
            } catch (ExecutionException e) {
                log.error("error get {}",key,e);
            }
        }

    }

    @Override
    public void del(String key) {
        LoadingCache<String, String> loadingCache = CACHE_MAP.get(key);
        if (loadingCache != null){
            loadingCache.invalidate(key);
            CACHE_MAP.remove(key);
        }
    }
}
