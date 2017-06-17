package com.absurd.rick.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author Absurd.
 */

public interface CacheService {
    <T> List<T> getList(String key, Class<T> clazz);


    <T> T getObject(String key, Class<T> clazz);


    <T> void setObject(String key, T result, int time, TimeUnit seconds);

    void touch(String key, int time, TimeUnit seconds);

    void del(String key);
}
