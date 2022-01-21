package com.hogwarts.dto;

import com.hogwarts.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口响应
 *
 * @author chushangming
 * @since 2022-01-21
 */
@Data
@AllArgsConstructor
public class ResDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private Boolean success;

    private String message;

    private T data;

    public ResDTO() {
    }

    private ResDTO(Integer code) {
        this(code, true, ResultCode.SUCCESS.getMessage(), null);
    }

    private ResDTO(Integer code, String message) {
        this(code, true, message, null);
    }

    private ResDTO(Integer code, T data) {
        this(code, true, ResultCode.SUCCESS.getMessage(), null);
    }

    private ResDTO(Integer code, String message, T data) {
        this(code, true, message, data);
    }

    private ResDTO(Integer code, Boolean success) {
        this(code, success, ResultCode.SUCCESS.getMessage(), null);
    }

    private ResDTO(Integer code, Boolean success, String message) {
        this(code, success, message, null);
    }

    private ResDTO(Integer code, Boolean success, T data) {
        this(code, success, ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResDTO<T> data(T data) {
        return data(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResDTO<T> data(String message, T data) {
        return data(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ResDTO<T> data(Integer code, String message, T data) {
        return new ResDTO(code, data == null ? "暂无承载数据" : message, data);
    }

    public static <T> ResDTO<T> success(Integer code) {
        return new ResDTO(code);
    }

    public static <T> ResDTO<T> success(Integer code, String message) {
        return new ResDTO(code, message);
    }

    public static <T> ResDTO<T> success(Integer code, String message, T data) {
        return new ResDTO(code, message, data);
    }

    public static <T> ResDTO<T> fail(Integer code) {
        return new ResDTO(code);
    }

    public static <T> ResDTO<T> fail(String message) {
        return new ResDTO(ResultCode.FAILURE.getCode(), message);
    }

    public static <T> ResDTO<T> fail(Integer code, String message) {
        return new ResDTO(code, message);
    }

    public static <T> ResDTO<T> status(boolean flag) {
        return flag ? success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage()) : fail(ResultCode.FAILURE.getMessage());
    }

    public String toString() {
        return "ResponseData(code=" + this.getCode() + ", success=" + this.getSuccess() + ", data=" + this.getData() + ", msg=" + this.getMessage() + ")";
    }
}
