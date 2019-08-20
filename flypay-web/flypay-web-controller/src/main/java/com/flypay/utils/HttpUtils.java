package com.flypay.utils;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2018/6/13.
 */

public class HttpUtils {
    private static final Logger LOGGER = Logger.getLogger(HttpUtils.class);
    // 注入HttpClient实例
    @Resource(name = "HttpClientManagerFactoryBen")
    private static CloseableHttpClient client;

    /**
     *  http请求
     *  发送xml格式的请求
     * @param method GET POST
     * @param url
     * @param header 请求头,默认有xml的传送格式
     * @param params xml参数
     * @param encode 编码格式
     * @return String 字符串(一般都是xml)
     */
    public static String sendRequest(Method method, String url, Map<String, String> header, String params,Encode encode){

        return parseHttpByteToString(send(method,url,header,params,encode),encode);
    }

    private static byte[] send(Method method, String url,Map<String, String> header, String params, Encode encode) {
            //get请求
        byte[] bytes = null;
        if( Method.GET.equals(method)){

        }else{
            //post请求
            bytes = doPost(url,header,params,encode);
        }
        return bytes;
    }
    /**
     * 发送post请求
     * 参数为xml
     */
    private static byte[] doPost(String url, Map<String, String> header, String params,Encode encode) {
        //构建POST请求   请求地址请更换为自己的。

        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            HttpPost post = new HttpPost(url);
            //使用之前写的方法创建httpClie createSSLClientDefault();
            // 构造消息头
            setHeader(post,header);
            // 构建消息实体
            StringEntity entity = new StringEntity(params, Charset.forName(encode.value));
            // 发送xml格式的数据请求
            entity.setContentType("text/xml");
            post.setEntity(entity);
            //发送请求
            response = client.execute(post);
            if( response == null ){
                LOGGER.error("URL :" + url + "=====响应为空,请检查网络");
                return null;
            }
            int status = response.getStatusLine().getStatusCode();
            if( 200 == status){
                HttpEntity responseEntity = response.getEntity();
                if( responseEntity == null){
                    LOGGER.error("URL :" + url + "=====响应异常,请检查网络-------1");
                    return null;
                }
                is = responseEntity.getContent();
                if( is == null){
                    LOGGER.error("URL :" + url + "=====响应异常,请检查网络-------2");
                    return null;
                }
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                int len = -1;
                byte[] tmp = new byte[2048];
                while ((len = is.read(tmp)) != -len) {
                    byteOut.write(tmp,0,len);
                }
                return byteOut.toByteArray();
            }else{
                LOGGER.error("URL :" + url + "=====响应错误,响应码 : " + status);
                return null;
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("URL :" + url + "====FileNotFoundException====" ,e);
        } catch (ClientProtocolException e) {
            LOGGER.error("URL :" + url + "====ClientProtocolException====" ,e);
        } catch (IOException e) {
            LOGGER.error("URL :" + url + "====IOException====" ,e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close InputStream Exception" ,e);
                }
            }
            if( response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("close CloseableHttpResponse Exception" ,e);
                }
            }
        }
        return null;
    }

    /**
     * 设置请求头
     * @param http
     * @param header
     */
    private static void setHeader(HttpEntityEnclosingRequestBase http, Map<String, String> header) {
        if( header == null && header.isEmpty()){
            header = jsonHeaders;
        }else{
            header.putAll(jsonHeaders);
        }
        // 设置头信息
        for (Map.Entry<String, String> entry : header.entrySet()) {
            http.addHeader(entry.getKey(), entry.getValue());
        }


    }

    public static enum Method{
        GET,POST;
    }
    public static enum Encode{
        UTF8("UTF-8");
        public String value;
        private Encode(String value) {
            this.value = value;
        }
    }
    public static final Map<String,String> jsonHeaders = new HashMap<String,String>();
    static{
        jsonHeaders.put("Content-Type", "text/xml; charset=UTF-8");
    }
    /**
     * 将字节数组解析为字符串
     * @param bytes
     * @return
     */
    private static String parseHttpByteToString(byte[] bytes,Encode encode){
        try {
            return new String(bytes,encode.value);
        } catch (Exception e) {
            LOGGER.error("http请求响应结果转字符串异常(" + encode.value + ") :" + bytes);
        }
        return null;
    }
}