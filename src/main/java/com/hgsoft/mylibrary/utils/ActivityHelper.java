package com.hgsoft.mylibrary.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Activity 跳转工具类
 */
public class ActivityHelper {

    /**
     * 跳转至下一个Activity，当前Activity仍保留
     *
     * @param curAct 当前Activity实例
     * @param toAct  下一跳Activity
     */
    public static <T> void toAct(Activity curAct, Class<T> toAct) {
        intentAct(curAct, toAct, null, null, false);
    }

    /**
     * 跳转至下一个Activity，当前Activity仍保留
     *
     * @param curAct 当前Activity实例
     * @param toAct  下一跳Activity
     * @param bundle 传给下一跳的参数
     */
    public static <T> void toAct(Activity curAct, Class<T> toAct, Bundle bundle) {
        intentAct(curAct, toAct, null, bundle, false);
    }

    /**
     * 替换当前Activity
     *
     * @param oldAct
     * @param newAct
     * @param bundle
     */
    public static <T> void replaceAct(Activity oldAct, Class<T> newAct, Bundle bundle) {
        intentAct(oldAct, newAct, null, bundle, true);
    }

    /**
     * 替换当前Activity，新Activity若在栈中存在，则它所有子Activity出栈
     *
     * @param oldAct 被替换的Activity实例
     * @param topAct 新Activity
     */
    public static <T> void replaceAct2Top(Activity oldAct, Class<T> topAct, Bundle bundle) {
        intentAct(oldAct, topAct, Intent.FLAG_ACTIVITY_CLEAR_TOP, bundle, true);
    }

    private static <T> void intentAct(Activity oldAct, Class<T> newAct, Integer flag, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(oldAct, newAct);
        if (flag != null) {
            intent.setFlags(flag.intValue());
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinish) {
            oldAct.finish();
        }
        oldAct.startActivity(intent);
    }

    public static void jumpToWebPage(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}

