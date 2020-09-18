# logback-loggrab
日志收集服务,独立部署。一键启动。即下即用,简单配置。

# 使用方法详见如下:
### 第一步:
###### 克隆项目到本地:
      git clone https://github.com/clockworm/logback-loggrab.git
#### 第二步:
###### 进入项目logback-loggrab目录下,执行编译打包命令:
        mvn clean -Dmaven.test.skip=true install package
######   通过maven管理查看源码打开logback-kafka包的logback-kafka.xml配置文件:
         Maven Dependencies
         |-- xx.jar
         |-- xx.jar
         `-- logback-kafka-x.x.x.jar
             |-- io.github.clockworm
             |-- META-INF
             `-- logback-kafka.xml
######   将logback-kafka.xml拷贝至自身服务的src/main/resources/目录下:
     `src/main/resources
         `-- application.properties
         `-- logback.xml
         `-- logback-kafka.xml
### 第四步:
######   自身服务的logback.xml或logback-spring.xml,引入logback-kafka.xml:
       <?xml version="1.0" encoding="UTF-8" ?>
       <!DOCTYPE XML>
       <configuration debug="false">
	         <include resource="logback-kafka.xml"/>
       ...

### 第五步:
######   集成完毕,启动项目
### 彩蛋:
######   日志收集服务logback-loggrab项目,将于近期开源,感谢关注该项目的所有开发者。
