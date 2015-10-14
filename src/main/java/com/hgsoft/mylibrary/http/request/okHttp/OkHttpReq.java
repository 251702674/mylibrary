package com.hgsoft.mylibrary.http.request.okHttp;


import android.text.TextUtils;

import com.hgsoft.mylibrary.http.request.BaseHttpReq;
import com.hgsoft.mylibrary.http.request.HttpReq;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * OkHttp 请求实现
 *
 * @author LinZheng
 */
public class OkHttpReq extends BaseHttpReq<Request> {

    private Request.Builder mBuilder;

    public OkHttpReq(String mUrl) {
        this(mUrl, HttpReq.GET);
    }

    public OkHttpReq(String mUrl, String mMethod) {
        this(mUrl, mMethod, null);
    }

    public OkHttpReq(String mUrl, String mMethod, Map<String, String> mParams) {
        this(mUrl, mMethod, mParams, null);
    }

    public OkHttpReq(String mUrl, String mMethod, Map<String, String> mParams, Map<String, String> mHeaders) {
        this(mUrl, mMethod, mParams, mHeaders, null);
    }

    public OkHttpReq(String mUrl, String mMethod, Map<String, String> mParams, Map<String, String> mHeaders, Map<String, Map<String, File>> mFileParams) {
        super(mUrl, mMethod, mParams, mHeaders, mFileParams);
        mBuilder = new Request.Builder();
    }


    @Override
    public Request buildReq() {
        if (validMethod(mMethod)) {
            try {
                addBody(mMethod, mUrl, mParams, mFileParams);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new IllegalArgumentException("illegal method, method must in = {GET, POST, PUT, DELETE, PATCH}");
        }
        addHeader(mHeaders, mCache);
        if (!TextUtils.isEmpty(TAG)) {
            mBuilder.tag(TAG);
        }
        return mBuilder.build();
    }

    /**
     * 校验请求方法和URL合法性
     *
     * @param method 请求方法
     * @return true or false
     */
    protected boolean validMethod(String method) {
        return method.equals(GET)
                || method.equals(POST)
                || method.equals(PATCH)
                || method.equals(PUT)
                || method.equals(DELETE);
    }

    /**
     * 构建请求
     *
     * @param method    请求方法
     * @param url       请求地址
     * @param strParams String类型参数(第一个String为参数名称：paramsName; 第二个 String为参数值：value)
     * @throws UnsupportedEncodingException
     */
    private void addBody(String method, String url, Map<String, String> strParams) throws UnsupportedEncodingException {
        if (method.equals(GET)) {   // GET 方法，拼接URL
            url = ParamsEncodingUtil.encodeUrl(url, strParams);
            mBuilder.url(url);
        } else {                    // 其他方法，构建请求体
            RequestBody requestBody = ParamsEncodingUtil.encodeBody(strParams);
            mBuilder.url(url).method(method, requestBody);
        }
    }

    /**
     * 构建请求,可提交文件
     *
     * @param method     请求方法
     * @param url        请求地址
     * @param strParams  String类型参数(第一个String为参数名称：paramsName; 第二个 String为参数值：value)
     * @param fileParams File类型参数(第一个String为参数名称：paramsName; 第二个 String为文件名：fileName)
     * @throws UnsupportedEncodingException
     */
    private void addBody(String method, String url, Map<String, String> strParams,
                         Map<String, Map<String, File>> fileParams) throws UnsupportedEncodingException {
        if (fileParams != null && fileParams.size() > 0) {
            RequestBody requestBody = ParamsEncodingUtil.encodeBody(strParams, fileParams);
            mBuilder.url(url);
            mBuilder.method(method, requestBody);
        } else {
            addBody(method, url, strParams);
        }
    }

    /**
     * 添加请求头
     *
     * @param headers 请求头内容
     * @param cache   是否缓存数据
     */
    private void addHeader(Map<String, String> headers, boolean cache) {

        if (!cache) {
            CacheControl cacheControl = new CacheControl.Builder().noCache().noStore().build();
            mBuilder.cacheControl(cacheControl);
        }

        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                mBuilder.addHeader(key, headers.get(key));
            }
        }
    }

}
