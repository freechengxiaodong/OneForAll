package com.hogwarts.http;

/**
 * HTTP请求客户端
 *
 * @author chushangming
 * @since 2022-01-21
 */
public interface HttpClient {

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public HttpResponse execute(HttpRequest request) throws Exception;
}
