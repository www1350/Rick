package com.absurd.rick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


/**
 * @author Absurd.
 */
@SpringBootApplication
@ServletComponentScan
public class SpringBootMybatisWithRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisWithRedisApplication.class,args);
    }
}
