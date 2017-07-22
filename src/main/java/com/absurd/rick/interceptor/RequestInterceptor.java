package com.absurd.rick.interceptor;

import com.absurd.rick.config.AuthHolder;
import com.absurd.rick.filter.RequestFilter;
import com.absurd.rick.util.AsmTools;
import com.absurd.rick.util.ContextUtil;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestInterceptor {


    private final static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private final static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Pointcut("execution(* com.absurd.rick.controller..*.*(..))")
    public void execute(){

    }

    @Before("execute()")
    public void getParams(JoinPoint point){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try{
            MethodSignature ms = (MethodSignature) point.getSignature();
            Method method = ms.getMethod();
            Object[] params = point.getArgs();
            if (params == null || params.length == 0) {
                return;
            }
            List<String> paramNames = AsmTools.getMethodParamNames(method);
            if (paramNames == null || paramNames.size() == 0) {
                return;
            }
            String[] paramJsons=new String[params.length];
            StringBuilder format = new StringBuilder();
            int total=params.length<paramNames.size()?params.length:paramNames.size();
            for (int i = 0; i <total; i++) {
                /**
                 * json输出mutipartfile太占屏
                 */
                if(params[i] instanceof MultipartFile || params[i] instanceof HttpServletRequest || params[i] instanceof HttpServletResponse){
                    continue;
                }
                paramJsons[i]= JSONObject.toJSONString(params[i]);
                format.append(paramNames.get(i)+ ":"+paramJsons[i]+",");
            }
            RequestFilter.reqJson.get().put("params",format.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @After("execute()")
    public void printLogAfter(JoinPoint point) {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        point.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = urlPathHelper.getLookupPathForRequest(request);
        JSONObject json = RequestFilter.reqJson.get();
        executorService.execute(new LoggerRunner(url,method,json, AuthHolder.uid(),AuthHolder.username()));
        ContextUtil.clear();
    }


    protected UrlPathHelper urlPathHelper = new UrlPathHelper();
    class LoggerRunner implements Runnable{

        private String url;

        private Method method;

        private JSONObject json;

        private String operatorId;

        private String operatorDisplay;

        public LoggerRunner(String url,Method method,JSONObject json,String operatorId,String operatorDisplay) {
            this.json = json;
            this.method = method;
            this.url = url;
            this.operatorId = operatorId;
            this.operatorDisplay = operatorDisplay;
        }


        @Override
        public void run() {
            try{
                StringBuffer sb = new StringBuffer();
                sb.append(json.get("method")).append(" ");
                if (!StringUtils.isEmpty(url)){
                    sb.append(url);
                }
                sb.append("--->");
                sb.append(method.getName());
                sb.append("--->");
                if(json.get("params")!=null){
                    sb.append("params->").append(json.get("params"));
                }
                sb.append("ip->").append(json.get("ip")).append(",");
                sb.append("refer->").append(json.get("refer")).append(",");
                sb.append("operatorId->").append(operatorId).append(",");
                sb.append("operator displayName->").append(operatorDisplay).append(",");
                sb.append("cost->").append(System.currentTimeMillis()-json.getLongValue("startTime")).append("ms");
                logger.info(sb.toString());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
