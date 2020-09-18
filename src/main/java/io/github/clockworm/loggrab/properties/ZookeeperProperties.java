package io.github.clockworm.loggrab.properties;

import lombok.Data;

@Data
public class ZookeeperProperties {
	private String servers;
	private String listenStartWith = "io_github_clockworm";
}
