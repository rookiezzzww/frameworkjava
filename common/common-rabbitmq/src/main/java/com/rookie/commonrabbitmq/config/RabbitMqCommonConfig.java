package com.rookie.commonrabbitmq.config;


import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbitmq配置
 */
@Configuration
public class RabbitMqCommonConfig {

    /**
     * 统一配置 RabbitMQ 消息的 JSON 转换规则，让业务代码方便地发送和接收 Java 对象
     * @return 序列化转换器
     */
    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        // 指定反序列化期间要信任的一组包，星号 ( * ) 表示全部信任
        defaultClassMapper.setTrustedPackages("*");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(defaultClassMapper);
        return converter;
    }

}
