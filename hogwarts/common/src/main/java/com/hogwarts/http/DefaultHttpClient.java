package com.hogwarts.http;

import com.alibaba.fastjson.JSON;
import com.hogwarts.enums.ResultCode;
import com.hogwarts.dto.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Http Client that based on Apache HttpClient component 4.0+
 *
 * @author chushangming
 * @since 2022-01-21
 */
@Service("DefaultHttpClient")
public class DefaultHttpClient implements HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpClient.class);

    @Override
    public HttpResponse execute(HttpRequest request) throws Exception {

        //创建HTTP客户端 初始化请求响应对象
        HttpRequestBase httpRequestBase;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = createHttpClient(request);
        DefaultHttpResponse httpResponse = new DefaultHttpResponse();
        //构建请求及请求数据
        switch (request.getMethod()) {
            case HttpMethod.GET:
                httpRequestBase = get(request);
                break;
            case HttpMethod.POST:
                httpRequestBase = post(request);
                break;
            case HttpMethod.POST_FORM:
                httpRequestBase = postFromData(request);
                break;
            case HttpMethod.POST_BY_URL_PARAMS:
                httpRequestBase = postByURLParams(request);
                break;
            case HttpMethod.PUT:
                httpRequestBase = put(request);
                break;
            case HttpMethod.DELETE:
                httpRequestBase = delete(request);
                break;
            case HttpMethod.DELETE_JSON:
                httpRequestBase = deleteJson(request);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP request method: " + request.getMethod());
        }

        //请求开始时间
        long start = System.currentTimeMillis();
        try {
            //设置超时时间及请求参数
            int timeout = 1000 * request.getTimeout();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
            httpRequestBase.setConfig(requestConfig);
            //执行HTTP请求
            response = httpClient.execute(httpRequestBase);
            //记录日志
            LOGGER.info("httpClient response {}", response);

            //响应状态码
            httpResponse.setStatusCode(response.getStatusLine().getStatusCode());
            //响应耗时
            long end = System.currentTimeMillis();
            httpResponse.setCostTime(end - start);
            //响应内容
            if (request.getByteResult()) {
                httpResponse.setByteContent(EntityUtils.toByteArray(response.getEntity()));
            } else {
                httpResponse.setContent(EntityUtils.toString(response.getEntity(), request.getCharset()));
            }
        } catch (IOException e) {
            //捕获响应
            Response resp = new Response();
            resp.setCode(ResultCode.TIME_OUT.getCode());
            resp.setMessage(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : ResultCode.TIME_OUT.getMessage());
            resp.setCostTime(System.currentTimeMillis() - start);
            //HTTP响应
            httpResponse.setStatusCode(ResultCode.TIME_OUT.getCode());
            httpResponse.setCostTime(resp.getCostTime());
            httpResponse.setContent(JSON.toJSONString(resp));
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("close http resources error.", e);
            }
        }

        LOGGER.info("response content = {}, cost time: {}ms.", request.getByteResult() ? httpResponse.getByteContent() : httpResponse.getContent(), httpResponse.getCostTime());
        return httpResponse;
    }

    /**
     * POST请求数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpPost post(HttpRequest request) throws Exception {
        HttpPost httpPost = new HttpPost(request.getURL());
        //设置HEADER
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        //请求数据准备
        Map<String, Object> paramObjectMap = request.getParamObjectMap();
        switch (request.getMimeType()) {
            case HttpMethod.MIME_TYPE_JSON:
                //JSON传递参数
                if (null != paramObjectMap && paramObjectMap.size() > 0) {
                    StringEntity stringEntity = new StringEntity(JSON.toJSONString(paramObjectMap), ContentType.create(request.getMimeType(), request.getCharset()));
                    httpPost.setEntity(stringEntity);
                }
                break;
            case HttpMethod.MIME_TYPE_FORM_DATA:
                //form-data类型传递参数
                if (null != paramObjectMap && paramObjectMap.size() > 0) {
                    List<NameValuePair> formParams = new ArrayList<>();
                    for (Entry<String, Object> entry : paramObjectMap.entrySet()) {
                        formParams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                    }
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                    httpPost.setEntity(entity);
                }
                break;
            default:
                //默认方式 MIME_TYPE_URLENCODED
                String requestContent = request.getContent();
                if (StringUtils.isNotBlank(requestContent)) {
                    StringEntity stringEntity = new StringEntity(requestContent, ContentType.create(request.getMimeType(), request.getCharset()));
                    httpPost.setEntity(stringEntity);
                }
                break;
        }

        LOGGER.info("HttpPost URL = {} ParamObjectMap = {}, Entity = {}", request.getURL(), request.getParamObjectMap(), httpPost.getEntity());
        return httpPost;
    }

    /**
     * POST URL请求数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpPost postByURLParams(HttpRequest request) throws Exception {

        HttpPost httpPost = new HttpPost(packEncodeURL(request));
        LOGGER.info("HttpPostByURLParams URL = {}", httpPost.getURI());
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        return httpPost;
    }

    /**
     * 表单图片上传
     *
     * @param request
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpPost postFromData(HttpRequest request) throws ClientProtocolException, IOException {
        //将所有需要上传元素打包成HttpEntity对象
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //分情况设置 addPart中的name是表单里的name属性 可以自定义
        if (StringUtils.isNotBlank(request.getFilePath())) {
            //第一种方式 装载本地上传图片的文件
            File imageFile = new File(request.getFilePath());
            FileBody imageFileBody = new FileBody(imageFile);
            builder.addPart("file[]", imageFileBody);
        } else if (StringUtils.isNotBlank(request.getFileBase64String())) {
            //第二种方式 装载经过base64编码的图片的数据
            String imageBase64Data = request.getFileBase64String();
            byte[] byteImage = Base64.decodeBase64(imageBase64Data);
            ByteArrayBody byteArrayBody = new ByteArrayBody(byteImage, "image_name");
            builder.addPart("byteArrayBody", byteArrayBody);
        } else if (StringUtils.isNotBlank(request.getFileString())) {
            //第三种方式 装载上传字符串的对象
            StringBody stringBody = new StringBody(request.getFileString(), ContentType.TEXT_PLAIN);
            builder.addPart("stringBody", stringBody);
        }
        //添加附加表单参数
        Map<String, Object> paramObjectMap = request.getParamObjectMap();
        if (null != paramObjectMap && paramObjectMap.size() > 0) {
            for (Entry<String, Object> entry : paramObjectMap.entrySet()) {
                builder.addPart(entry.getKey(), new StringBody(String.valueOf(entry.getValue()), ContentType.create(request.getMimeType(), Consts.UTF_8)));
            }
        }
        //创建HttpPost对象，用于包含信息发送post消息
        HttpPost httpPost = new HttpPost(request.getURL());
        //设置Header属性
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        //构建HttpEntity对象
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);

        LOGGER.info("postFromData URL = {} ParamObjectMap = {}, Entity = {}", request.getURL(), request.getParamObjectMap(), httpPost.getEntity());
        return httpPost;
    }

    /**
     * PUT请求数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpPut put(HttpRequest request) throws Exception {
        HttpPut httpPut = new HttpPut(request.getURL());
        //设置HEADER
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpPut.setHeader(entry.getKey(), entry.getValue());
            }
        }

        //请求数据准备
        Map<String, Object> paramObjectMap = request.getParamObjectMap();
        switch (request.getMimeType()) {
            case HttpMethod.MIME_TYPE_JSON:
                //JSON传递参数
                if (null != paramObjectMap && paramObjectMap.size() > 0) {
                    StringEntity stringEntity = new StringEntity(JSON.toJSONString(paramObjectMap), ContentType.create(request.getMimeType(), request.getCharset()));
                    httpPut.setEntity(stringEntity);
                }
                break;
            case HttpMethod.MIME_TYPE_FORM_DATA:
                //form-data类型传递参数
                if (null != paramObjectMap && paramObjectMap.size() > 0) {
                    List<NameValuePair> formParams = new ArrayList<>();
                    for (Entry<String, Object> entry : paramObjectMap.entrySet()) {
                        formParams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                    }
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                    httpPut.setEntity(entity);
                }
                break;
            default:
                //默认方式 MIME_TYPE_URLENCODED
                String requestContent = request.getContent();
                if (StringUtils.isNotBlank(requestContent)) {
                    StringEntity stringEntity = new StringEntity(requestContent, ContentType.create(request.getMimeType(), request.getCharset()));
                    httpPut.setEntity(stringEntity);
                }
                break;
        }

        LOGGER.info("HttpPut ParamObjectMap = {}, Entity = {}", request.getParamObjectMap(), httpPut.getEntity());
        return httpPut;
    }

    /**
     * GET请求数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpGet get(HttpRequest request) throws Exception {
        HttpGet httpGet = new HttpGet(packGetURL(request));
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        LOGGER.info("HttpGet URL = {}", request.getURL());
        return httpGet;
    }

    /**
     * DELETE请求数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpDelete delete(HttpRequest request) throws Exception {
        HttpDelete httpDelete = new HttpDelete(packGetURL(request));
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpDelete.setHeader(entry.getKey(), entry.getValue());
            }
        }

        LOGGER.info("HttpDelete URL = {}", request.getURL());
        return httpDelete;
    }

    /**
     * DELETE请求数据  json传递参数
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpDeleteWithBody deleteJson(HttpRequest request) throws Exception {
        //创建DELETE BODY
        HttpDeleteWithBody httpDeleteWithBody = new HttpDeleteWithBody(request.getURL());
        //设置Content-Type
        httpDeleteWithBody.setHeader("Content-Type", "application/json;charset=UTF-8");
        //设置header
        if (null != request.getHeaderMap() && request.getHeaderMap().size() > 0) {
            Set<Entry<String, String>> set = request.getHeaderMap().entrySet();
            for (Entry<String, String> entry : set) {
                httpDeleteWithBody.setHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数
        if (request.getParamObjectMap() != null && request.getParamObjectMap().size() > 0) {
            //写入JSON数据参数
            httpDeleteWithBody.setEntity(new StringEntity(JSON.toJSONString(request.getParamObjectMap())));
        }

        LOGGER.info("HttpDeleteWithBody URL = {} ParamObjectMap = {}", request.getURL(), request.getParamObjectMap());
        return httpDeleteWithBody;
    }

    /**
     * Pack the URL and parameters
     *
     * @return
     */
    protected String packGetURL(HttpRequest httpRequest) {
        StringBuilder sb = new StringBuilder(httpRequest.getURL());
        if (httpRequest.getParamMap().size() > 0) {
            if (sb.indexOf("?") == -1) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            for (Entry<String, String> param : httpRequest.getParamMap().entrySet()) {
                if (StringUtils.isNotBlank(param.getValue())) {
                    sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
                }
            }
            if (sb.lastIndexOf("&") != -1) {
                sb.deleteCharAt(sb.lastIndexOf("&"));
            }
        }

        httpRequest.setURL(sb.toString());
        return httpRequest.getURL();
    }

    /**
     * Pack the URL and parameters
     *
     * @return
     * @throws Exception
     */
    protected String packEncodeURL(HttpRequest httpRequest) throws Exception {
        StringBuilder sb = new StringBuilder(httpRequest.getURL());
        if (httpRequest.getParamMap().size() > 0) {
            if (sb.indexOf("?") == -1) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            for (Entry<String, String> param : httpRequest.getParamMap().entrySet()) {
                if (StringUtils.isNotBlank(param.getValue())) {
                    sb.append(URLEncoder.encode(param.getKey(), httpRequest.getCharset())).append("=").append(URLEncoder.encode(param.getValue(), httpRequest.getCharset())).append("&");
                }
            }
            if (sb.lastIndexOf("&") != -1) {
                sb.deleteCharAt(sb.lastIndexOf("&"));
            }
        }

        httpRequest.setURL(sb.toString());
        return httpRequest.getURL();
    }

    /**
     * 创建HTTP客户端
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected CloseableHttpClient createHttpClient(HttpRequest request) throws Exception {
        boolean ssl = request.getSSL();
        if (ssl) {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }
        return HttpClients.createDefault();
    }
}
