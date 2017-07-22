package com.absurd.rick.filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wangwenwei on 17/7/22.
 */
@WebFilter(urlPatterns = "/*", filterName = "requestFilter")
public class RequestFilter implements Filter{
    public final static ThreadLocal<JSONObject> reqJson = new ThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        JSONObject json=new JSONObject();
        json.put("method",request.getMethod());
        json.put("uri", request.getRequestURI());
        json.put("ip",request.getRemoteAddr());
        json.put("refer",request.getHeader("referer"));
        json.put("startTime",System.currentTimeMillis());
        reqJson.set(json);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}
