package com.hogwarts.http;

import java.io.Serializable;

/**
 * HTTP请求响应数据
 *
 * @author chushangming
 * @since 2022-01-21
 */
public interface HttpResponse extends Serializable {

    public Integer getStatusCode();

    public String getContent();

    public byte[] getByteContent();

    public Long getCostTime();

    public void setCostTime(Long costTime);
}
