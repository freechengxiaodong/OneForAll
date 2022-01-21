package com.hogwarts.http;

import java.io.Serializable;
import java.util.Map;

/**
 * HTTP请求参数
 *
 * @author chushangming
 * @since 2022-01-21
 */
public interface HttpRequest extends Serializable {

    public void setProvider(String provider);

    public String getProvider();

    public void setServiceName(String serviceName);

    public String getServiceName();

    public Long getReqTime();

    public void setReqTime(Long reqTime);

    public void setURL(String url);

    public String getURL();

    public void setMethod(String method);

    public String getMethod();

    public void setHeaderMap(Map<String, String> headerMap);

    public Map<String, String> getHeaderMap();

    public Map<String, String> getParamMap();

    public Map<String, Object> getParamObjectMap();

    public void setMimeType(String mimeType);

    public String getMimeType();

    public void setCharset(String charset);

    public String getCharset();

    public void setTimeout(int timeout);

    public int getTimeout();

    public void setSSL(boolean ssl);

    public boolean getSSL();

    public void setByteResult(boolean byteResult);

    public boolean getByteResult();

    /**
     * 获取字符串内容
     *
     * @return
     * @throws Exception
     */
    public String getContent() throws Exception;

    /**
     * 直接设置请求报文
     *
     * @param content
     */
    public void setContent(String content);

    public String getFilePath();

    public void setFilePath(String filePath);

    public String getFileString();

    public void setFileString(String fileString);

    public String getFileBase64String();

    public void setFileBase64String(String fileBase64String);

    /**
     * 添加数字签名并返回
     *
     * @param key
     * @return signature
     * @throws Exception
     */
    public String sign(String key) throws Exception;
}
