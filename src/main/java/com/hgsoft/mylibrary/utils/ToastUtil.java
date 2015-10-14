package com.hgsoft.mylibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast显示工具
 */
public class ToastUtil {

    /**
     * 显示Toast（Toast.LENGTH_LONG）
     *
     * @param resId 显示的文字
     */
    public static void show(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast（Toast.LENGTH_LONG）
     *
     * @param text 显示的文字
     */
    public static void show(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示Toast（Toast.LENGTH_LONG）
     *
     * @param resId 显示的文字
     */
    public static void showLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Toast（Toast.LENGTH_LONG）
     *
     * @param text 显示的文字
     */
    public static void showLong(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
