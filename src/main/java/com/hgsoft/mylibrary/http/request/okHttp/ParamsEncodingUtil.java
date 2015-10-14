package com.hgsoft.mylibrary.http.request.okHttp;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author LinZheng
 */
public final class ParamsEncodingUtil {

    /**
     * 将请求参数，译码至URL上(如： GET 方法请求)
     *
     * @param url    请求连接
     * @param params 键值对 参数
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeUrl(String url, Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder encodeUrl = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            StringBuilder paramsEncoding = new StringBuilder();
            for (String key : params.keySet()) {
                if (paramsEncoding.length() > 0) {
                    paramsEncoding.append("&");
                }
                paramsEncoding.append(URLEncoder.encode(key, "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
            encodeUrl.append("?").append(paramsEncoding.toString());
        }
        return encodeUrl.toString();
    }

    /**
     * 构建请求体，将请求参数String类型转成RequestBody
     *
     * @param params 键值对 参数
     * @return
     */
    public static RequestBody encodeBody(Map<String, String> params) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                formEncodingBuilder.add(key, params.get(key));
            }
            return formEncodingBuilder.build();
        } else {
            return null;
        }
    }

    /**
     * 构建请求体， 将请求参数放入RequestBody(该方法用于文件上传)(Content-Type : multipart/form-data)
     *
     * @param strParams  Map<String, String> 第一个String 为参数名(paramsName)，第二个String 为参数值(value)
     * @param fileParams Map<String, Map<String, File>> fileParams 第一个String为参数名(paramsName)，第二个String 是文件名(fileName)
     * @return {@link com.squareup.okhttp.MultipartBuilder}
     */
    public static RequestBody encodeBody(Map<String, String> strParams, Map<String, Map<String, File>> fileParams) {
        MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (strParams != null && strParams.size() > 0) {
            for (String paramsName : strParams.keySet()) {
                multipartBuilder.addFormDataPart(paramsName, strParams.get(paramsName));
            }
        }
        if (fileParams != null && fileParams.size() > 0) {
            for (String paramsName : fileParams.keySet()) {
                Map<String, File> fileMap = fileParams.get(paramsName);
                if (fileMap != null && fileMap.size() > 0) {
                    for (String fileName : fileMap.keySet()) {
                        RequestBody body = RequestBody.create(null, fileMap.get(fileName));
                        multipartBuilder.addFormDataPart(paramsName, fileName, body);
                    }
                }
            }
        }
        return multipartBuilder.build();
    }
}
