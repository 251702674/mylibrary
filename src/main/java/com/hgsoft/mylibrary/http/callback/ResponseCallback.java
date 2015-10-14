package com.hgsoft.mylibrary.http.callback;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 回调函数
 *
 * @param <T> 返回类型
 */
public abstract class ResponseCallback<T> {

    private Type mType;

    public ResponseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public Type getType() {
        return mType;
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public abstract void onResponseSuccess(int what, int statusCode, T result, Response response);

    public abstract void onResponseError(int what, int statusCode, String string, Response response);

    public abstract void onFailure(int what, String failureMsg, Request request, Exception e);


}
