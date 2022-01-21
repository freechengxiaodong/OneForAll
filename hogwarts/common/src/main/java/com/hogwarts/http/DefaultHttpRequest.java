package com.hogwarts.http;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * HTTP请求参数
 *
 * @author chushangming
 * @since 2022-01-21
 */
public class DefaultHttpRequest implements HttpRequest {

    private static final long serialVersionUID = 8257485417037263157L;

    private String serviceName;

    private String provider;

    private Long reqTime;

    private String url;

    private String method = HttpMethod.POST;

    private String charset = "UTF-8";

    private String mimeType = "application/x-www-form-urlencoded";

    private int timeout = 60; //单位：秒

    private boolean ssl = false;

    private boolean byteResult = false;

    protected Map<String, String> headerMap;

    protected final Map<String, String> paramMap = new TreeMap<String, String>();

    protected final Map<String, Object> paramObjectMap = new TreeMap<String, Object>();

    private String content;

    private String filePath;

    private String fileString;

    private String fileBase64String;

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public Long getReqTime() {
        return reqTime;
    }

    @Override
    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void setURL(String url) {
        this.url = url;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    @Override
    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, Object> getParamObjectMap() {
        return paramObjectMap;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean getSSL() {
        return this.ssl;
    }

    @Override
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public void setByteResult(boolean byteResult) {
        this.byteResult = byteResult;
    }

    @Override
    public boolean getByteResult() {
        return this.byteResult;
    }

    @Override
    public String getContent() throws Exception {
        if (StringUtils.isNotBlank(this.content)) {
            return this.content;
        }

        if (paramObjectMap.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> entry : paramObjectMap.entrySet()) {
            String value = String.valueOf(entry.getValue());
            if (!StringUtils.isBlank(value)) {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(value, charset)).append("&");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf("&"));
        return sb.toString();
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
    }

    public String getFileBase64String() {
        return fileBase64String;
    }

    public void setFileBase64String(String fileBase64String) {
        this.fileBase64String = fileBase64String;
    }

    /**
     * Override this method for custom sign.
     */
    @Override
    public String sign(String key) throws Exception {
        //do nothing by default. overwrite it if need to do something signature.
        return key;
    }

    @Override
    public String toString() {
        return "DefaultHttpRequest{" +
                "serviceName='" + serviceName + '\'' +
                ", provider='" + provider + '\'' +
                ", reqTime=" + reqTime +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", charset='" + charset + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", timeout=" + timeout +
                ", ssl=" + ssl +
                ", byteResult=" + byteResult +
                ", headerMap=" + headerMap +
                ", paramMap=" + paramMap +
                ", paramObjectMap=" + paramObjectMap +
                ", content='" + content + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileBase64String='" + fileBase64String + '\'' +
                '}';
    }
}
