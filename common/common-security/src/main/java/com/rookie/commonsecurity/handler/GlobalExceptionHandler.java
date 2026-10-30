package com.rookie.commonsecurity.handler;

/**
 * 全局异常处理，统一返回格式
 * RestControllerAdvice+ExceptionHandler: 统一处理异常
 *
 */

import com.rookie.commondomain.domain.R;
import com.rookie.commondomain.domain.ResultCode;
import com.rookie.commondomain.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * - @RestControllerAdvice：让该类为多个 Controller 提供统一处理，并将返回值写入响应体，通常是 JSON。
 * - @ExceptionHandler(ServiceException.class)：指定这个方法处理 ServiceException 及其子类。
 *  流程就是：业务代码抛出 ServiceException
 * → Spring 调用匹配的异常处理方法
 * → 返回统一的 R 格式给前端
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 设置http响应码：从我们定义的ResultCode中获取前三位对应给http响应码
     *
     * @param response 响应信息
     * @param errcode 响应码
     */
    private void setResponseCode(HttpServletResponse response,Integer errcode){
        int httpCode = Integer.parseInt(String.valueOf(errcode).substring(0,3));
        response.setStatus(httpCode);
    }

    /**
     * 请求方式不支持
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 异常结果
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        setResponseCode(response, ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode());
        return R.fail(ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode(), ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getMsg());
    }


    /**
     * 类型不匹配异常
     *
     * @param e 异常信息
     * @param response 响应
     * @return 不匹配结果
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public R<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                          HttpServletResponse response) {
        log.error("类型不匹配异常",e);
        setResponseCode(response, ResultCode.PARA_TYPE_MISMATCH.getCode());
        return R.fail(ResultCode.PARA_TYPE_MISMATCH.getCode(), ResultCode.PARA_TYPE_MISMATCH.getMsg());
    }

    /**
     * url未找到异常
     *
     * @param e 异常信息
     * @param response 响应
     * @return 异常结果
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public R<?> handleMethodNoResourceFoundException(NoResourceFoundException e,
                                                     HttpServletResponse response) {
        log.error("url未找到异常",e);
        setResponseCode(response, ResultCode.URL_NOT_FOUND.getCode());
        return R.fail(ResultCode.URL_NOT_FOUND.getCode(), ResultCode.URL_NOT_FOUND.getMsg());
    }

    /**
     * 业务异常
     *
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 业务异常结果
     */
    @ExceptionHandler(ServiceException.class)
    public R<?> handleServiceException(ServiceException e, HttpServletRequest request,
                                       HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生业务异常", requestURI,  e);
        setResponseCode(response,e.getCode());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 异常报文
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request,
                                                      HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生参数校验异常", requestURI, e);
        setResponseCode(response, ResultCode.INVALID_PARA.getCode());
        String message = joinMessage(e);
        return R.fail(ResultCode.INVALID_PARA.getCode(), message);
    }

    /**
     * 拼接校验不合法的信息，比如同时有几个参数不合法，就用逗号拼接返回
     */
    private String joinMessage(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getAllErrors();
        if (CollectionUtils.isEmpty(allErrors)) {
            return "";
        }
        return allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
    }

    /**
     * 参数校验异常，这里主要针对参数约束异常，比如@Size、@Min、@Max等
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 异常报文
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public R<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request,
                                                      HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}', 发生参数校验异常",requestURI, e);
        setResponseCode(response,ResultCode.INVALID_PARA.getCode());
        String message = e.getMessage();
        return R.fail(ResultCode.INVALID_PARA.getCode(),message);
    }


    /**
     * 拦截运行时异常
     *
     * @param e 异常信息
     * @param request 请求信息
     * @param response 响应信息
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException e, HttpServletRequest request,
                                       HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生运行时异常.", requestURI, e);
        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }

    /**
     * 系统异常，这个是用来拦截没有被捕获的异常，兜底
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生异常.", requestURI, e);
        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }
}
