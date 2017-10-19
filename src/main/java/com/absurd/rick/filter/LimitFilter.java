package com.absurd.rick.filter;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流
 * Created by wangwenwei on 17/7/21.
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "requestFilter")
public class LimitFilter implements Filter {

    private static final int MAX_COUNT = 20000;

    private final static AtomicInteger FILTER_COUNT = new AtomicInteger(0);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("before {}",FILTER_COUNT);
        if (FILTER_COUNT.get() > MAX_COUNT) {
            //请求个数太多，跳转到排队页面
            servletRequest.getRequestDispatcher("index.html").forward(servletRequest, servletResponse);
        } else {
            //请求个数加1
            FILTER_COUNT.incrementAndGet();
            filterChain.doFilter(servletRequest, servletResponse);
            //访问结束，请求个数减1
            FILTER_COUNT.decrementAndGet();
        }
    }




    @Override
    public void destroy() {

    }
}