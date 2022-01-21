package com.hogwarts.http;

/**
 * HTTP请求方式
 *
 * @author chushangming
 * @since 2022-01-21
 */
public interface HttpMethod {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String POST_FORM = "POST_FORM";
    public static final String POST_BY_URL_PARAMS = "POST_BY_URL_PARAMS";
    public static final String PUT = "PUT";
    public static final String HEAD = "HEAD";
    public static final String DELETE = "DELETE";
    public static final String DELETE_JSON = "DELETE_JSON";
    public static final String TRACE = "TRACE";

    //请求编码
    public static final String MIME_TYPE_JSON = "application/json";
    public static final String MIME_TYPE_FORM_DATA = "multipart/form-data";
    public static final String MIME_TYPE_URLENCODED = "application/x-www-form-urlencoded";

}
