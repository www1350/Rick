package com.absurd.rick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * @author Absurd.
 */
@SpringBootApplication
@ServletComponentScan
public class SpringBootMybatisWithRedisApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        setRegisterErrorPageFilter(false);
        return builder.sources(SpringBootMybatisWithRedisApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisWithRedisApplication.class,args);
    }
}
