package com.siiruo.wscheduler.core.util;

import com.siiruo.wscheduler.core.type.RequestType;
import com.siiruo.wscheduler.core.type.ResponseType;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by siiruo wong on 2020/1/9.
 */
public final class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String ENCODING = "UTF-8";
    private static final int CONNECT_TIMEOUT = 6000;
    private static final int SOCKET_TIMEOUT = 6000;
    private static final Map<String,String> JSON_HEADERS = new HashMap<>();
    static {
        JSON_HEADERS.put("connection","Keep-Alive");
        JSON_HEADERS.put("Content-Type","application/json;charset=UTF-8");
        JSON_HEADERS.put("Accept-Charset","application/json;charset=UTF-8");
    }
    private HttpClientUtil(){}
    public static class HttpClientResult implements Serializable {

        /**
         * 响应状态码
         */
        private int code;

        /**
         * 响应数据
         */
        private String content;

        private Throwable ex;

        public HttpClientResult(int code) {
            this.code = code;
        }

        public HttpClientResult(int code, String content) {
            this.code = code;
            this.content = content;
        }

        public HttpClientResult(int code, String content, Throwable ex) {
            this.code = code;
            this.content = content;
            this.ex = ex;
        }
    }

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * 发送get请求；带请求参数
     *
     * @param url 请求地址
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, null, params);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(buildParamURI(url,params));
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);
        buildHeader(headers, httpGet);
        return doRequest(httpClient,httpGet);
    }

    /**
     * 发送post请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url) throws Exception {
        return doPost(url, null, null,null);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url 请求地址
     * @param formData 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, Map<String, String> formData) throws Exception {
        return doPost(url, null, null,formData);
    }


    /**
     *
     * @param url
     * @param request
     * @param clazz
     * @param <R>
     * @return
     * @throws Exception
     */
    public static <R extends ResponseType> R doPost(String url, RequestType request, Class<R> clazz) throws Exception {
        HttpClientResult result = doPost(url, JSON_HEADERS, null, request);
        if (result.code== HttpStatus.SC_OK) {
            return StringUtil.isBlank(result.content) ? null : JsonUtil.toBean(result.content, clazz);
        }
        return null;
    }


    /**
     * 发送post请求；带请求头和请求参数
     *  formData 和 body 只能二选其一，同时headers必须是与之对应的配置
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @param body body
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params,Object body) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(buildParamURI(url,params));
        buildHeader(headers, httpPost);
        if(body!=null){
            if (body instanceof Map) {
                buildFormData((Map<String,String>)body, httpPost);
            }else if(body instanceof String){
                StringEntity requestEntity = new StringEntity((String) body,ENCODING);
                requestEntity.setContentEncoding(ENCODING);
                httpPost.setEntity(requestEntity);
            } else {
                StringEntity requestEntity = new StringEntity(JsonUtil.toJSONString(body),ENCODING);
                requestEntity.setContentEncoding(ENCODING);
                httpPost.setEntity(requestEntity);
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        return doRequest(httpClient,httpPost);
    }

    /**
     * 发送put请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPut(String url) throws Exception {
        return doPut(url);
    }

    /**
     * 发送put请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPut(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);
        buildFormData(params, httpPut);
        return doRequest(httpClient,httpPut);
    }

    /**
     * 发送delete请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doDelete(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpDelete.setConfig(requestConfig);
        return doRequest(httpClient,httpDelete);
    }

    /**
     * 发送delete请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("_method", "delete");
        return doPost(url, params);
    }

    /**
     * Description: 封装请求头
     * @param headers
     * @param httpMethod
     */
    public static void buildHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        if (headers != null&&!headers.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param formData
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public static void buildFormData(Map<String, String> formData, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if (formData != null&&!formData.isEmpty()) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = formData.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    public static URI buildParamURI(String basicUrl, Map<String, String> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(basicUrl);
        if (params!=null&&!params.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder.build();
    }


    private static HttpClientResult doRequest(CloseableHttpClient httpClient, HttpRequestBase httpMethod){
        HttpClientResult result;
        CloseableHttpResponse httpResponse=null;
        try {
            httpResponse = httpClient.execute(httpMethod);
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                String content = "";
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
                result=new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
            }else{
                result=new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,"no response");
            }
        } catch (IOException e) {
            LOGGER.error("an IOException occurs when executing request.",e);
            result=new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,"failure",e);
        } catch (ParseException e) {
            LOGGER.error("a ParseException occurs when executing request.",e);
            result=new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,"failure",e);
        }finally {
            if (httpResponse!=null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    LOGGER.error("an IOException occurs when closing response connection.",e);
                    //do not affect the current result
                }
            }
            if (httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    LOGGER.error("an IOException occurs when closing client connection.",e);
                    //do not affect the current result
                }
            }
        }
        return result==null?new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,"no response"):result;
    }
}
