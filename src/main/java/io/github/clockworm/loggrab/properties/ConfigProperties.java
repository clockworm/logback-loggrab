package io.github.clockworm.loggrab.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "io.github.clockworm.middle.loggrab")
@Component
@Data
public class ConfigProperties {
	private String logPath;
	private KafkaProperties kafka;
	private ZookeeperProperties zook;
}
