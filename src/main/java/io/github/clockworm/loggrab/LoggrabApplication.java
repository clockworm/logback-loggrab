package io.github.clockworm.loggrab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoggrabApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoggrabApplication.class, args);
	}

}
