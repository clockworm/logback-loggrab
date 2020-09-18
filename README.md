# logback-loggrab
日志收集服务,独立部署。一键启动。即下即用,简单配置。
##### 温馨提示:  使用前请确认日志发送的服务使用logback-kafka组件

# 部署指南如下:
### 第一步:
###### 克隆项目到本地:
      git clone https://github.com/clockworm/logback-loggrab.git
### 第二步:
######   进入项目修改application.properties修改kafka和zookeeper配置地址:
       io.github.clockworm.middle.loggrab.kafka.servers=xxx.xxx.xxx.xxx:9092
       io.github.clockworm.middle.loggrab.zook.servers=xxx.xxx.xxx.xxx:2181
#### 第三步:
###### 进入项目logback-loggrab目录下,执行编译打包命令:
        mvn clean -Dmaven.test.skip=true install package
### 第四步:
######   配置完毕,启动项目
        java -jar logback-loggrab.jar
### 第五步:
######   进入目录查看其它服务日志是否被收集到该服务器上
         cd /data/kafka/logs
