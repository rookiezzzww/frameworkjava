package com.rookie.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class})
public class FileServiceApplication {
    /**
     * 启动函数
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
        log.info("文件服务启动成功!");
    }
}
