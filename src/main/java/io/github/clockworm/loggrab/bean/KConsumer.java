package io.github.clockworm.loggrab.bean;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.github.clockworm.loggrab.factory.KafkaConsumerFactory;
import io.github.clockworm.loggrab.factory.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
public class KConsumer extends Thread {
    private final String topic;
    private final Consumer<String, String> consumer;

    public KConsumer(String topic, String hostServer) {
        this.topic = topic;
        this.consumer = KafkaConsumerFactory.getConsumer(hostServer);
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList(topic));
            String[] split = topic.split("_");
            Logger logger = LoggerFactory.getLogger(split[3], split[4]);
            log.info("准备收集项目组{}服务{}的日志", split[3], split[4]);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200));
                for (ConsumerRecord<String, String> record : records) {
                    logHandler(record, logger);
                }
            }
        } finally {
            consumer.close();
        }
    }

    private void logHandler(ConsumerRecord<?, ?> record, Logger log) {
        String logLevel = record.key().toString();
        String logMessage = record.value().toString();
        if (Level.INFO.toString().equals(logLevel)) {
            log.info(logMessage);
        } else {
            log.error(logMessage);
        }
    }

}