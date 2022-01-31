package io.github.clockworm.loggrab.scheduled;

import io.github.clockworm.loggrab.bean.KConsumer;
import io.github.clockworm.loggrab.properties.ConfigProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TopicScheduled {

    public final static Set<String> topics = new HashSet<String>();

    @Autowired
    private ZooKeeper zkClient;
    @Autowired
    private ConfigProperties configProperties;

    @Scheduled(cron = "0/15 * * * * ?")
    public @SneakyThrows
    void searchLogTopic() {
        KConsumer kConsumer = null;
        try {
            List<String> children = zkClient.getChildren("/brokers/topics", null);
            for (String topic : children) {
                if (!StringUtils.startsWithIgnoreCase(topic, configProperties.getZook().getListenStartWith())) continue;
                if (!topics.add(topic)) continue;
                String[] split = topic.split("_");
                log.info("添加{}项目组{}服务日志收集[初始化]", split[3], split[4]);
                kConsumer = new KConsumer(topic, configProperties.getKafka().getServers());
                kConsumer.start();
                log.info("添加{}项目组{}服务日志收集[启动完毕]", split[3], split[4]);
            }
        } catch (Exception e) {
            log.error("添加项目组服务日志收集异常:{}", e);
        }
    }
}