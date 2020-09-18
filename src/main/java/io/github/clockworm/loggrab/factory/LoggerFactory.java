package io.github.clockworm.loggrab.factory;

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.OptionHelper;

public class LoggerFactory {

	private static final Map<String, Logger> container = new HashMap<>();

	/** 获取日志组件*/
	public static Logger getLogger(String projectName,String serverName) {
		Logger logger = container.get(projectName.concat(serverName));
		if (logger != null) {
			return logger;
		}
		synchronized (LoggerFactory.class) {
			logger = container.get(projectName.concat(serverName));
			if (logger != null) {
				return logger;
			}
			logger = build(projectName,serverName);
			container.put(projectName.concat(serverName), logger);
		}
		return logger;
	}

	/**动态生成日志文件 滚动日志*/
	private static Logger build(String projectName,String serverName) {
		LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern("%msg%n");
		encoder.start();

		//  INFO日志
		RollingFileAppender<ILoggingEvent> INFO = new RollingFileAppender<ILoggingEvent>();
		INFO.setContext(context);
		INFO.setAppend(true);
		INFO.setPrudent(false);
		TimeBasedRollingPolicy<ILoggingEvent> infoTimeBasedRollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
		infoTimeBasedRollingPolicy.setFileNamePattern(OptionHelper.substVars("${ROOT_PATH}" + "/" + projectName + "/" + serverName + "/info.%d{yyyy-MM-dd}.log", context));
		infoTimeBasedRollingPolicy.setMaxHistory(30);
		infoTimeBasedRollingPolicy.setParent(INFO);
		infoTimeBasedRollingPolicy.setContext(context);
		infoTimeBasedRollingPolicy.start();
		INFO.setRollingPolicy(infoTimeBasedRollingPolicy);
		INFO.setEncoder(encoder);
		INFO.start();

		//  ERROR日志
		RollingFileAppender<ILoggingEvent> ERROR = new RollingFileAppender<ILoggingEvent>();
		ERROR.setContext(context);
		ERROR.setAppend(true);
		ERROR.setPrudent(false);
		TimeBasedRollingPolicy<ILoggingEvent> errorTimeBasedRollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
		errorTimeBasedRollingPolicy.setFileNamePattern(OptionHelper.substVars("${ROOT_PATH}" + "/" + projectName + "/" + serverName + "/error.%d{yyyy-MM-dd}.log", context));
		errorTimeBasedRollingPolicy.setMaxHistory(30);
		errorTimeBasedRollingPolicy.setParent(ERROR);
		errorTimeBasedRollingPolicy.setContext(context);
		errorTimeBasedRollingPolicy.start();
		ERROR.setRollingPolicy(errorTimeBasedRollingPolicy);
		ERROR.setEncoder(encoder);
		ERROR.start();

		ThresholdFilter warnThresholdFilter = new ThresholdFilter();
		warnThresholdFilter.setLevel("WARN");
		warnThresholdFilter.setContext(context);
		warnThresholdFilter.start();
		ERROR.addFilter(warnThresholdFilter);

		//设置日志组件Appender
		Logger logger = context.getLogger(projectName.concat(serverName));
		logger.setAdditive(false);
		logger.addAppender(INFO);
		logger.addAppender(ERROR);
		logger.setLevel(Level.INFO);
		return logger;
	}

}