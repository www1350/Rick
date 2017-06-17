package com.absurd.rick.cache.resolver;

import com.absurd.rick.cache.CacheService;
import com.absurd.rick.annotation.Cache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * @author Absurd.
 */
@Slf4j
@Aspect
@Component
public class CacheResolver {

    @Resource(name = "redisCache")
    private CacheService cacheService;

    @Around("@annotation(com.absurd.rick.annotation.Cache)")
    public Object resolver(ProceedingJoinPoint point){
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            Object[] args = point.getArgs();
            String className = point.getTarget().getClass().getSimpleName();
            String methodName = point.getSignature().getName();
            Cache cache = method.getAnnotation(Cache.class);
            String key = className + methodName + cache.key();
            for (Object object : args) {
                if (object instanceof HttpServletRequest || object instanceof HttpSession) {
                    continue;
                }
                key += "-" + object;
            }
            Object result = null;
        try {
            if (method.getReturnType().getName().contains("java.util.List")) {
                result = cacheService.getList(key, cache.clazz());
            }else if(!(cache.clazz().getName().contains("java.lang.Object"))){
                result=cacheService.getObject(key,cache.clazz());
            }else{
                result=cacheService.getObject(key,method.getReturnType());
            }

            if (result == null) {
                result = point.proceed();
                if (result != null) {
                        cacheService.setObject(key, result, cache.time(), TimeUnit.SECONDS);
                    }
            }
        }catch (Throwable throwable) {
            log.error("can't load cache",throwable);
        }
        return result;


    }

}
