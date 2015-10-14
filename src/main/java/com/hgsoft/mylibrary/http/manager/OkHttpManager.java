package com.hgsoft.mylibrary.http.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hgsoft.mylibrary.http.callback.FileDownloadCallback;
import com.hgsoft.mylibrary.http.callback.ResponseCallback;
import com.hgsoft.mylibrary.http.interceptor.LogInterceptor;
import com.hgsoft.mylibrary.http.request.HttpReq;
import com.hgsoft.mylibrary.utils.ImageUtils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * OkHttp 辅助类实现
 */
public class OkHttpManager implements HttpManager<Request> {

    public static final String TAG = "MyOkHttpClient";
    public static final int DEFAULT_CONNECT_TIMEOUT = 13; // 默认超时时间
    public static long DEFAULT_HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 默认缓存大小
    public static final String DISK_CACHE_NAME = "myHttpCache";

    private static OkHttpManager mInstance;
    protected OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Logger mLogger;
    private Cache mCache; // 缓存
    private Gson mGson;

    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.interceptors().add(new LogInterceptor());
        mHandler = new Handler(Looper.getMainLooper());
        mLogger = Logger.getLogger(OkHttpManager.class.getName());
        mGson = new Gson();
    }

    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化 OkHttp 辅助类配置
     *
     * @param context 上下文
     */
    public void initialize(Context context) {
        configureCache(context);
        configureTimeouts();
    }

    /**
     * 配置缓存
     */
    protected void configureCache(Context context) {
        if (mCache == null) {
            mCache = createHttpClientCache(context);
        }
        mOkHttpClient.setCache(mCache);
    }

    /**
     * 创建缓存
     *
     * @param context 上下文
     * @return 缓存
     */
    protected Cache createHttpClientCache(Context context) {
        File cacheDir = context.getDir(DISK_CACHE_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, cacheDir.getPath());
        return new Cache(cacheDir, DEFAULT_HTTP_CACHE_SIZE);
    }

    /**
     * 配置化超时时间
     */
    protected void configureTimeouts() {
        mOkHttpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
    }

    //*************对外公布的方法************

    public static void doHttp(HttpReq<Request> req, ResponseCallback callback) {
        getInstance().deliveryResult(req, 0, callback);
    }

    public static void doHttp(HttpReq<Request> req, int what, ResponseCallback callback) {
        getInstance().deliveryResult(req, what, callback);
    }

    public static void displayImage(final ImageView view, String url, int errorResId) throws IOException {
        getInstance()._displayImage(view, url, errorResId);
    }


    public static void displayImage(final ImageView view, String url) {
        getInstance()._displayImage(view, url, -1);
    }

    public static void downloadAsync(String url, String destDir, ResponseCallback callback) {
        getInstance()._downloadAsync(url, destDir, callback);
    }

    public static void fileDownload(int what, String url, String destDir, String fileName, FileDownloadCallback callback) {
        getInstance()._fileDownloadAsync(what, url, destDir, fileName, callback);
    }

    public static void cancel(int what) {
        getInstance()._cancelRequest(what);
    }

    //**************************************

    /**
     * 分发消息
     *
     * @param httpReq  自定义请求体
     * @param what     请求标识
     * @param callback 回调函数
     */
    @Override
    public void deliveryResult(HttpReq<Request> httpReq, final int what, final ResponseCallback callback) {
        Request request = httpReq.buildReq();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String failureMsg = "网络异常！";
                if (e instanceof InterruptedIOException) {
                    failureMsg = "连接超时！";
                } else if (e instanceof UnknownServiceException) {
                    failureMsg = "服务异常！";
                } else if (e instanceof UnknownHostException) {
                    failureMsg = "服务器异常！";
                } else if (e instanceof ConnectException) {
                    failureMsg = "服务器连接失败，请检查网络！";
                } else if (e instanceof SocketException) {
                    failureMsg = "网络异常！";
                }
                sendFailedStringCallback(what, failureMsg, e, request, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                int statusCode = response.code();
                final String string = response.body().string();
                mLogger.info(String.format("Received response for %s%n statusCode = %s%n%s", response.request().urlString(), statusCode, string));
                if (response.isSuccessful()) { // 请求成功
                    if (callback.getType() == String.class) {
                        sendSuccessResultCallback(what, statusCode, string, response, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.getType());
                        sendSuccessResultCallback(what, statusCode, o, response, callback);
                    }
                } else {
                    sendErrorResultCallback(what, statusCode, string, response, callback);
                }
                // TODO 异常处理
            }

        });
    }

    /**
     * 分发请求失败消息(超时，连接失败，网络异常)
     *
     * @param what       用于标识请求类型
     * @param failureMsg 失败提示信息
     * @param e          异常
     * @param request    请求
     * @param callback   回调函数
     */
    private void sendFailedStringCallback(final int what, final String failureMsg, final Exception e, final Request request, final ResponseCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(what, failureMsg, request, e);
                }
            }
        });
    }

    /**
     * 发送请求成功的消息(statusCode 在区间 [200..300) 的结果)
     *
     * @param what       用于标识请求类型
     * @param statusCode 状态码
     * @param object     返回类型
     * @param response   回应
     * @param callback   回调函数
     */
    private void sendSuccessResultCallback(final int what, final int statusCode, final Object object, final Response response, final ResponseCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponseSuccess(what, statusCode, object, response);
                }
            }
        });
    }

    /**
     * 分发请求错误的消息
     *
     * @param what       用于标识请求类型
     * @param statusCode 状态码
     * @param string     内容
     * @param response   回应
     * @param callback   回调函数
     */
    private void sendErrorResultCallback(final int what, final int statusCode, final String string, final Response response, final ResponseCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponseError(what, statusCode, string, response);
                }
            }
        });
    }


    /**
     * 同步的Get请求
     *
     * @param url 连接地址
     * @return Response
     */
    private Response _getAsync(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }


    /**
     * 异步下载文件
     *
     * @param url         下载地址
     * @param destFileDir 本地文件存储的文件夹
     * @param callback    回调函数
     */
    private void _downloadAsync(final String url, final String destFileDir, final ResponseCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(0, "网络异常", e, request, callback);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第三个参数为文件的绝对路径
                    sendSuccessResultCallback(0, 200, file.getAbsolutePath(), response, callback);
                } catch (IOException e) {
                    sendFailedStringCallback(0, "网络异常", e, request, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 文件下载
     *
     * @param url         下载地址
     * @param destFileDir 文件路径
     * @param callback    回调
     */
    private void _fileDownloadAsync(int what, final String url, final String destFileDir, final String fileName, final FileDownloadCallback callback) {
        final Request request = new Request.Builder()
                .url(url).tag(what)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onDownloadFail(request, e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                if (response.isSuccessful()) {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    try {
                        long size = response.body().contentLength();
                        long downloadSize = 0;
                        is = response.body().byteStream();
                        final File file = new File(destFileDir, fileName);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            downloadSize += len;
                            if (callback != null) {
                                callback.onDownloadProgress(downloadSize, size, request);
                            }
                        }
                        fos.flush();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //如果下载文件成功，第一个参数为文件的绝对路径
                                if (callback != null) {
                                    callback.onDownloadFinish(file.getAbsolutePath(), response);
                                }
                            }
                        });
                    } catch (final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onDownloadFail(request, e);
                                }
                            }
                        });
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onDownloadFail(request, null);
                    }
                }
            }
        });
    }

    /**
     * 加载图片
     *
     * @param view 图片
     * @param url  图片URL
     */
    private void _displayImage(final ImageView view, final String url, final int errorResId) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                    ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                    int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                    try {
                        is.reset();
                    } catch (IOException e) {
                        response = _getAsync(url);
                        is = response.body().byteStream();
                    }

                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inJustDecodeBounds = false;
                    ops.inSampleSize = inSampleSize;
                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    setErrorResId(view, errorResId);

                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

    private void _cancelRequest(int what) {
        mOkHttpClient.cancel(what);
    }

}
