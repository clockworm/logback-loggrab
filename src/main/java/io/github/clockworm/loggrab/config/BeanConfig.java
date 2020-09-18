package io.github.clockworm.loggrab.config;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.clockworm.loggrab.properties.ConfigProperties;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BeanConfig {
	
	@Autowired
	private ConfigProperties configProperties;

	@Bean(name = "zkClient")
	public ZooKeeper zkClient() {
		ZooKeeper zooKeeper = null;
		try {
			final CountDownLatch countDownLatch = new CountDownLatch(1);
			zooKeeper = new ZooKeeper(configProperties.getZook().getServers(), 3000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (Event.KeeperState.SyncConnected == event.getState()) {
						countDownLatch.countDown();
					}
				}
			});
			countDownLatch.await();
			log.info("初始化ZooKeeper连接状态:{}", zooKeeper.getState());
		} catch (Exception e) {
			log.error("初始化ZooKeeper连接异常:{}", e);
		}
		return zooKeeper;
	}

}