package io.github.clockworm.loggrab.factory;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaConsumerFactory {

	final static String key_serializer = "org.apache.kafka.common.serialization.StringSerializer";
	final static String value_serializer = "org.apache.kafka.common.serialization.StringSerializer";
	final static String key_deserializer = "org.apache.kafka.common.serialization.StringDeserializer";
	final static String value_deserializer = "org.apache.kafka.common.serialization.StringDeserializer";

	public static KafkaConsumer<String, String> getConsumer(String bootstrapServers) {
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("allow.auto.create.topics", false);
		props.put("group.id", "io.github.clockworm.middle.loggrab");
		props.put("client.id", UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "100");
		props.put("auto.offset.reset","latest");
		props.put("key.deserializer", key_deserializer);
		props.put("value.deserializer", value_deserializer);
		return new KafkaConsumer<String, String>(props);
	}
}
