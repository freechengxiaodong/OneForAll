package com.payment.utils;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
//import org.springblade.core.tool.utils.ObjectUtil;
import com.payment.enums.IResultCode;
import com.payment.enums.ResultCode;

/*@ApiModel(
    description = "返回信息"
)*/
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /*@ApiModelProperty(
        value = "状态码",
        required = true
    )*/
    private int code;
    /* @ApiModelProperty(
         value = "是否成功",
         required = true
     )*/
    private boolean success;
    /*@ApiModelProperty("承载数据")*/
    private T data;
    /*@ApiModelProperty(
        value = "返回消息",
        required = true
    )*/
    private String msg;

    private ResponseData(IResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private ResponseData(IResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private ResponseData(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private ResponseData(IResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private ResponseData(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.getCode() == code;
    }

    /*public static boolean isSuccess(@Nullable ResponseData<?> result) {
        return (Boolean)Optional.ofNullable(result).map((x) -> {
            return ObjectUtil.nullSafeEquals(ResultCode.SUCCESS.code, x.code);
        }).orElse(Boolean.FALSE);
    }*/

    /*public static boolean isNotSuccess(@Nullable ResponseData<?> result) {
        return !isSuccess(result);
    }*/

    public static <T> ResponseData<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> ResponseData<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> ResponseData<T> data(int code, T data, String msg) {
        return new ResponseData(code, data, data == null ? "暂无承载数据" : msg);
    }

    public static <T> ResponseData<T> success(String msg) {
        return new ResponseData(ResultCode.SUCCESS, msg);
    }

    public static <T> ResponseData<T> success(IResultCode resultCode) {
        return new ResponseData(resultCode);
    }

    public static <T> ResponseData<T> success(IResultCode resultCode, String msg) {
        return new ResponseData(resultCode, msg);
    }

    public static <T> ResponseData<T> fail(String msg) {
        return new ResponseData(ResultCode.FAILURE, msg);
    }

    public static <T> ResponseData<T> fail(int code, String msg) {
        return new ResponseData(code, (Object) null, msg);
    }

    public static <T> ResponseData<T> fail(IResultCode resultCode) {
        return new ResponseData(resultCode);
    }

    public static <T> ResponseData<T> fail(IResultCode resultCode, String msg) {
        return new ResponseData(resultCode, msg);
    }

    public static <T> ResponseData<T> status(boolean flag) {
        return flag ? success("操作成功") : fail("操作失败");
    }

    public int getCode() {
        return this.code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "ResponseData(code=" + this.getCode() + ", success=" + this.isSuccess() + ", data=" + this.getData() + ", msg=" + this.getMsg() + ")";
    }

    public ResponseData() {
    }
}
