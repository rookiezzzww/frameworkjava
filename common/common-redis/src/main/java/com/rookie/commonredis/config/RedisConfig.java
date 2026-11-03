package com.rookie.commonredis.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.rookie.commondomain.constants.CommonConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * redis配置
 * Redis 并不认识 Java 的 User 类。发送数据前，需要把对象转换为字节；
 * 读取后，再将字节转换为 Java 中可以使用的数据，这就是序列化和反序列化。
 */
@Configuration
public class RedisConfig {

    /**
     *RedisTemplate 默认通常使用 JDK 序列化，
     * 而你希望 key 是可读字符串、value 使用 JSON 格式，所以进行了自定义
     * @return redis操作模板
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //普通 key 和 hashKey 按字符串编码
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //普通 value 和 hashValue 按 JSON 格式编码
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = createJacksonSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        //初始化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 创建序列化器，createJacksonSerializer() 则进一步规定 JSON 转换细节。
     * @return redis序列化器
     */
    private GenericJackson2JsonRedisSerializer createJacksonSerializer() {
        ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
                //遇到未知字段时不报错
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //避免将日期转换为时间戳
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                //避免序列化空对象
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //避免无效的子类型
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                //避免将日期键转换为时间戳
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
                //避免使用注解
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                //添加时间模块
                .addModule(new JavaTimeModule())
                //添加 LocalDateTime 序列化和反序列化器
                .addModule(new SimpleModule()
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CommonConstants.STANDARD_FORMAT)))
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CommonConstants.STANDARD_FORMAT)))
                )
                //设置日期格式
                .defaultDateFormat(new SimpleDateFormat(CommonConstants.STANDARD_FORMAT))
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
        return new GenericJackson2JsonRedisSerializer(OBJECT_MAPPER);
    }
}
