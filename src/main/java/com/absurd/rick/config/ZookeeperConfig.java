package com.absurd.rick.config;

import com.absurd.rick.zookeeper.ZooKeeperOperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangwenwei on 17/6/27.
 */
@Configuration
public class ZookeeperConfig {
    @Value("${zookeeper.address}")
    private String address;

    @Value("#{T(java.lang.Integer).parseInt(${zookeeper.session-timeout}) }")
    private int sessionTimeout;

    @Value("#{T(java.lang.Integer).parseInt(${zookeeper.connection-timeout}) }")
    private int connectionTimeout;

    @Bean
    public ZooKeeperOperator zooKeeperOperator(){
        ZooKeeperOperator zk = new ZooKeeperOperator();
        zk.setAddress(address);
        zk.setSessionTimeout(sessionTimeout);
        zk.setConnectionTimeout(connectionTimeout);
        return zk;
    }
}
