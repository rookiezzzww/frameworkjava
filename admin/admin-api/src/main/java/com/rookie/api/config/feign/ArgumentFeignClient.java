package com.rookie.api.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 参数管理Feign客户端
 */
@FeignClient(contextId ="argumentFeignClient" ,value = "bite-admin",path = "/argument")
public interface ArgumentFeignClient {
}
