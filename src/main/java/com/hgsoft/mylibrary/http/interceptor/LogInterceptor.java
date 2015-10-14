package com.hgsoft.mylibrary.http.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author LinZheng
 *         拦截器，打印日志
 */
public class LogInterceptor implements Interceptor {
    private static final Logger logger = Logger.getLogger(LogInterceptor.class.getName());

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logger.info(String.format("Sending request %s%n on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        return chain.proceed(request);
    }


}
