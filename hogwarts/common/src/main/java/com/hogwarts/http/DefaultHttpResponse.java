package com.hogwarts.http;

/**
 * HTTP请求响应数据
 *
 * @author chushangming
 * @since 2022-01-21
 */
public class DefaultHttpResponse implements HttpResponse {

    private static final long serialVersionUID = -1855164090599824709L;

    private Integer statusCode;

    private String content;

    private byte[] byteContent;

    private Long costTime;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    @Override
    public Long getCostTime() {
        return costTime;
    }

    @Override
    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }
}
