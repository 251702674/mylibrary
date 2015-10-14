package com.hgsoft.mylibrary.http.request;

/**
 * HttpRequest 接口
 *
 * @author LinZheng
 */
public interface HttpReq<T> {

    String GET = "GET";
    String POST = "POST";
    String DELETE = "DELETE";
    String PUT = "PUT";
    String PATCH = "PATCH";

    /**
     * 构建请求
     * @return T
     */
    T buildReq();

}
