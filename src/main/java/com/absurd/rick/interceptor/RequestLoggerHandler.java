package com.absurd.rick.interceptor;

import com.absurd.rick.config.AuthHolder;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangwenwei on 2017/8/22.
 */
@Slf4j
public class RequestLoggerHandler extends HandlerInterceptorAdapter {
    private ThreadLocal<JSONObject> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        JSONObject json=new JSONObject();
        json.put("method",request.getMethod());
        json.put("uri", request.getRequestURI());
        json.put("ip",request.getRemoteAddr());
        json.put("refer",request.getHeader("referer"));
        json.put("startTime",System.currentTimeMillis());
        startTime.set(json);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        JSONObject json = startTime.get();
        StringBuffer sb = new StringBuffer();
        sb.append("uri->").append(json.get("uri"));
        sb.append("--->");
        if(json.get("params")!=null){
            sb.append("params->").append(json.get("params"));
        }
        sb.append("ip->").append(json.get("ip")).append(",");
        sb.append("refer->").append(json.get("refer")).append(",");
        sb.append("operator->").append(AuthHolder.uid()).append(",");
        sb.append("cost->").append(System.currentTimeMillis()-json.getLongValue("startTime")).append("ms");
        log.info(sb.toString());
        startTime.remove();

    }
}
