package com.hogwarts.dto;

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
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -6558726241871697134L;

    /**
     * 状态码
     */
    protected Integer code;
    /**
     * 响应信息
     */
    protected String message;

    /**
     * 花费时间
     */
    protected Long costTime;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public Response() {
        this(0, "操作成功!", 0L, null);
    }

    /**
     * 只返回code状态 默认状态码为 0，提示信息为“操作成功！”
     *
     * @param code
     */
    public Response(Integer code) {
        this(code, "操作成功!", 0L, null);
    }

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     *
     * @param code
     * @param message
     */
    public Response(Integer code, String message) {
        this(code, message, 0L, null);
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     *
     * @param code
     * @param message
     * @param data
     */
    public Response(Integer code, String message, T data) {
        this(code, message, 0L, data);
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     *
     * @param message
     * @param costTime
     * @param data
     */
    public Response(String message, Long costTime, T data) {
        this(0, message, costTime, data);
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param data
     */
    public Response(T data) {
        this(0, "操作成功!", 0L, data);
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param costTime
     * @param data
     */
    public Response(Long costTime, T data) {
        this(0, "操作成功!", costTime, data);
    }
}