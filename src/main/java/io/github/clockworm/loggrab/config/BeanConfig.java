package io.github.clockworm.loggrab.config;

import io.github.clockworm.loggrab.factory.ZookFactory;
import io.github.clockworm.loggrab.properties.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BeanConfig {

    @Autowired
    private ConfigProperties configProperties;

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() {
        try {
            ZookFactory zookFactory = new ZookFactory();
            ZooKeeper zooKeeper = zookFactory.connection(configProperties.getZook().getServers());
            return zooKeeper;
        } catch (Exception e) {
            log.error("初始化ZooKeeper连接异常:{}", e);
        }
        throw new RuntimeException("初始化ZooKeeper连接异常");
    }

}