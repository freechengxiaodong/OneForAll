package com.tools.exception;

import com.alibaba.fastjson.JSONObject;
import com.tools.enums.ResultCode;
import com.tools.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Component
public class ExceptionHandlerAdvice {

    /**
     * 处理Validated校验异常
     * <p>
     * 注: 常见的ConstraintViolationException异常， 也属于ValidationException异常
     *
     * @param exception 捕获到的异常
     * @return 返回给前端的data
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Response handleValidationException(Exception exception) {

        String message = null;
        log.error(" handleValidationException has been invoked", exception);

        if (exception instanceof BindException) {
            // BindException getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) exception).getFieldError();
            message = fieldError != null ? fieldError.getDefaultMessage() : null;
        } else if (exception instanceof MethodArgumentNotValidException) {
            // MethodArgumentNotValidException
            BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            //异常逻辑处理
            if (bindingResult.hasErrors()) {
                StringBuilder sb = new StringBuilder();
                List<ObjectError> errors = bindingResult.getAllErrors();
                if (errors != null) {
                    errors.forEach(p -> {
                        FieldError fieldError = (FieldError) p;
                        sb.append("Data check failure : object{")
                                .append(fieldError.getObjectName())
                                .append("},field{")
                                .append(fieldError.getField())
                                .append("},errorMessage{")
                                .append(fieldError.getDefaultMessage())
                                .append("}");
                    });

                    //记录日志
                    log.info("记录日志 {}", sb);
                }
            }

            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = bindingResult.getFieldError();
            message = fieldError != null ? fieldError.getDefaultMessage() : null;
        } else if (exception instanceof ConstraintViolationException) {
            // ValidationException 的子类异常ConstraintViolationException
            //ConstraintViolationException的e.getMessage()形如 {方法名}.{参数名}: {message} 这里只需要取后面的message即可
            message = exception.getMessage();
            if (message != null) {
                int lastIndex = message.lastIndexOf(':');
                if (lastIndex >= 0) {
                    message = message.substring(lastIndex + 1).trim();
                }
            }
        } else {
            // ValidationException 的其它子类异常
            message = "处理参数时异常";
        }

        return new Response(ResultCode.PARAM_VALID_ERROR.getCode(), "".equals(message) ? "请填写正确信息" : message, null);
    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public Response parameterTypeException(HttpMessageConversionException exception) {
        log.error(exception.getCause().getLocalizedMessage());
        return new Response(ResultCode.PARAM_VALID_ERROR.getCode(), "类型转换错误", null);
    }

    // 参数格式异常处理
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response badRequestException(IllegalArgumentException exception) {
        log.error("参数格式不合法：" + exception.getMessage());
        return new Response<>(HttpStatus.BAD_REQUEST.value() + "", "参数格式不符！");
    }

    // 权限不足异常处理
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response badRequestException(AccessDeniedException exception) {
        return new Response(HttpStatus.FORBIDDEN.value() + "", exception.getMessage());
    }

    // 参数缺失异常处理
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response badRequestException(Exception exception) {
        return new Response(HttpStatus.BAD_REQUEST.value() + "", "缺少必填参数！");
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleTypeMismatchException(NullPointerException ex) {
        log.error("空指针异常，{}", ex.getMessage());
        return new Response("500", "空指针异常");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleUnexpectedServer(Exception ex) {
        log.error("系统异常：", ex);
        return new Response("500", "系统发生异常，请联系管理员");
    }

    // 系统异常处理
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Throwable throwable) {
        log.error("系统异常", throwable);
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value() + "系统异常，请联系管理员！");
    }
}