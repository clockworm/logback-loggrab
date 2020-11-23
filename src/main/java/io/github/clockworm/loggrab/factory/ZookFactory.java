package io.github.clockworm.loggrab.factory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookFactory {


	final CountDownLatch countDownLatch = new CountDownLatch(1);

	ZooKeeper zooKeeper = null;

	public ZooKeeper connection(String servers) {
		try {
			if (zooKeeper != null) return zooKeeper;
			zooKeeper = new ZooKeeper(servers, 3000, new ZookeeperWatcher(servers));
			countDownLatch.await();
			log.info("初始化ZooKeeper连接状态:{}", zooKeeper.getState());
		} catch (Exception e) {
			log.error("初始化ZooKeeper连接异常:{}", e);
		}
		return zooKeeper;
	}

	class ZookeeperWatcher implements Watcher {
		@Getter
		private String servers;

		public ZookeeperWatcher(String servers) {
			this.servers = servers;
		}

		@Override
		public void process(WatchedEvent event) {
			if (Event.KeeperState.SyncConnected == event.getState()) {
				countDownLatch.countDown();
			} else if (Event.KeeperState.Expired == event.getState()) {
				try {
					log.info("zook session expried, start restart connection ...");
					zooKeeper = new ZooKeeper(this.servers, 3000, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
