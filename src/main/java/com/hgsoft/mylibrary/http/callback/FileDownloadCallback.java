package com.hgsoft.mylibrary.http.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 文件下载回调
 */
public interface FileDownloadCallback {

    void onDownloadFail(Request request, IOException e);

    void onDownloadProgress(long downloadSize, long size, Request request);

    void onDownloadFinish(String absolutePath, Response response);
}
