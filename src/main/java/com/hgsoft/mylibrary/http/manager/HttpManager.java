package com.hgsoft.mylibrary.http.manager;


import com.hgsoft.mylibrary.http.callback.ResponseCallback;
import com.hgsoft.mylibrary.http.request.HttpReq;

/**
 * Http 辅助类接口
 */
public interface HttpManager<T> {

    /**
     * 分发请求
     *
     * @param httpReq  自定义请求体
     * @param what     请求标识
     * @param callback 回调
     */
    void deliveryResult(HttpReq<T> httpReq, int what, ResponseCallback callback);
}
