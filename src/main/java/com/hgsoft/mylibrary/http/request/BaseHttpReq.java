package com.hgsoft.mylibrary.http.request;

import java.io.File;
import java.util.Map;

/**
 * @author LinZheng
 *         OkHttp请求体封装
 */
public abstract class BaseHttpReq<T> implements HttpReq<T> {

    public static final String BASE_URL = "http://220.197.219.6:8080/dataplatform"; // 云服务器
//    public static final String BASE_URL = "http://10.173.235.114:8080/dataplatform"; // 开发环境
//    public static final String BASE_URL = "http://10.173.235.176:9100/dataplatform"; // 测试环境
    protected String TAG;
    protected String mUrl;
    protected String mMethod;
    protected Map<String, String> mParams;
    protected Map<String, String> mHeaders;
    protected Map<String, Map<String, File>> mFileParams;
    protected boolean mCache = false;

    public BaseHttpReq(String mUrl) {
        this(mUrl, HttpReq.GET);
    }

    public BaseHttpReq(String mUrl, String mMethod) {
        this(mUrl, mMethod, null);
    }

    public BaseHttpReq(String mUrl, String mMethod, Map<String, String> mParams) {
        this(mUrl, mMethod, mParams, null);
    }

    public BaseHttpReq(String mUrl, String mMethod, Map<String, String> mParams, Map<String, String> mHeaders) {
        this(mUrl, mMethod, mParams, mHeaders, null);
    }

    public BaseHttpReq(String mUrl, String mMethod, Map<String, String> mParams, Map<String, String> mHeaders, Map<String, Map<String, File>> mFileParams) {
        this.mUrl = BASE_URL + mUrl;
        this.mMethod = mMethod;
        this.mParams = mParams;
        this.mHeaders = mHeaders;
        this.mFileParams = mFileParams;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    public Map<String, Map<String, File>> getFileParams() {
        return mFileParams;
    }

    public void setFileParams(Map<String, Map<String, File>> fileParams) {
        mFileParams = fileParams;
    }

    public boolean isCache() {
        return mCache;
    }

    public void setCache(boolean cache) {
        mCache = cache;
    }
}
