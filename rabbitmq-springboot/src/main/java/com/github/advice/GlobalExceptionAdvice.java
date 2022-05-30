package com.github.advice;

import com.github.rest.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-01-14 14:49
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    /**
     * 捕获 Exception 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        log.info("GlobalExceptionAdvice ===> handleException 的异常信息是 {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理上传文件太大的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        log.info("GlobalExceptionAdvice ===> handleMaxUploadSizeExceededException 的异常信息是 {}", ex.getMessage());
        return Result.error("文件体积太大");
    }


    /**
     * 捕获 NoHandlerFoundException 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handle404Exception(Exception ex) {
        log.info("GlobalExceptionAdvice ===> handle404Exception 的异常信息是 {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理 form data方式调用接口校验失败抛出的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String msg = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        log.info("GlobalExceptionAdvice ===> handleBindException 的异常信息是 {}", msg);
        return Result.error(ex.getMessage());
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String msg = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        log.info("GlobalExceptionAdvice ===> handleMethodArgumentNotValidException 的异常信息是 {}", msg);
        return Result.error(ex.getMessage());
    }


}
