package com.rookie.gateway.handler;

import com.rookie.commoncore.utils.ServletUtil;
import com.rookie.commondomain.domain.ResultCode;
import com.rookie.commondomain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 网关的异常可能发生在过滤、路由、网络转发等阶段，你当前使用的是 WebFlux 异常处理机
 请求进入网关
 → 网关过滤或转发过程中发生异常
 → GatewayExceptionHandler.handle() 接住
 → 判断响应是否已经提交
 → 确定 HTTP 状态码、业务码和消息
 → ServletUtil 构造 R，并转成 JSON 字节
 → writeWith() 写入响应
 → 返回 Mono<Void>，表示写出操作的完成情况
 */
@Order(-1) //指定异常处理器的顺序，数值越小越靠前
@Configuration
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * 处理器
     *
     * @param exchange ServerWebExchange
     * @param ex 异常信息
     * @return 无
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        //响应已经提交到客户端，无法再对这个响应进行常规的异常处理修改了，直接返回一个包含原始异常ex的Mono.error(ex)
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        //根据异常类型，设置返回码和返回消息，默认为ERROR
        int retCode = ResultCode.ERROR.getCode();
        String retMsg = ResultCode.ERROR.getMsg();
        //NoResourceFoundException	返回 404000、“服务未找到”
        if (ex instanceof NoResourceFoundException) {
            retCode = ResultCode.SERVICE_NOT_FOUND.getCode();
            retMsg = ResultCode.SERVICE_NOT_FOUND.getMsg();
        }
        //ServiceException	使用异常自带的错误码和消息
        else if (ex instanceof ServiceException) {
            retMsg = ex.getMessage();
            retCode = ((ServiceException) ex).getCode();
        }

        int httpCode = Integer.parseInt(String.valueOf(retCode).substring(0,3));

        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().
                getPath(), ex.getMessage());
        //交给工具方法输出
        return ServletUtil.webFluxResponseWriter(response, HttpStatus.valueOf(httpCode),retMsg, retCode);
    }
}