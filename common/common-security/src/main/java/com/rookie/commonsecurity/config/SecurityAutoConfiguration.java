package com.rookie.commonsecurity.config;

import com.rookie.commonsecurity.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(GlobalExceptionHandler.class)
//以后这个公共模块还要供 WebFlux 网关或非 Web 程序使用，加上此条件，避免加载不适用的配置或配置冲突
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityAutoConfiguration {
}
