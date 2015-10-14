package com.hgsoft.mylibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * 屏幕工具
 */
public class ScreenUtil {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static int dip2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static class CoordinateUtil {

        //
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        static double a = 6378245.0;
        static double ee = 0.00669342162296594323;

        /**
         * GPS转高德坐标
         */
        public LatLng gpsToGaode(LatLng latLng) {
            LatLng result = new LatLng();
            if (outOfChina(latLng.latitude, latLng.longitude)) {
                result.latitude = latLng.latitude;
                result.longitude = latLng.longitude;
                return result;
            }
            double dLat = transformLat(latLng.longitude - 105.0, latLng.latitude - 35.0);
            double dLon = transformLon(latLng.longitude - 105.0, latLng.latitude - 35.0);
            double radLat = latLng.latitude / 180.0 * Math.PI;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
            result.latitude = latLng.latitude + dLat;
            result.longitude = latLng.longitude + dLon;
            return result;
        }

        private boolean outOfChina(double lat, double lon) {
            if (lon < 72.004 || lon > 137.8347) {
                return true;
            }
            if (lat < 0.8293 || lat > 55.8271) {
                return true;
            }
            return false;
        }

        private double transformLat(double x, double y) {
            double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
            ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
            ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
            ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
            return ret;
        }

        private double transformLon(double x, double y) {
            double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
            ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
            ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
            ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
            return ret;
        }

        /**
         * 百度坐标转高德坐标
         */
        public LatLng baiduToGaode(LatLng latLng) {
            LatLng result = new LatLng();
            double x = latLng.longitude;
            double y = latLng.latitude;
            double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
            double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
            result.longitude = z * Math.cos(theta) + 0.0065;
            result.latitude = z * Math.sin(theta) + 0.006;
            return result;
        }

        /**
         * 高德坐标转百度坐标
         */
        public LatLng gaodeToBaidu(LatLng latLng) {
            LatLng result = new LatLng();
            double x = latLng.longitude - 0.0065, y = latLng.latitude - 0.006;
            double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
            double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
            result.longitude = z * Math.cos(theta);
            result.longitude = z * Math.sin(theta);
            return result;
        }

        static class LatLng {
            public double latitude;
            public double longitude;

            public LatLng() {
            }

            public LatLng(double longitude, double latitude) {
                this.latitude = latitude;
                this.longitude = longitude;
            }
        }

        public static void main(String[] args) {
            LatLng result;
            CoordinateUtil coordinateUtil = new CoordinateUtil();
            LatLng orgin = new LatLng(105.5638583, 25.93497499);
            result = coordinateUtil.gpsToGaode(orgin);
            System.out.println(result.longitude + "," + result.latitude);
            result = coordinateUtil.baiduToGaode(orgin);
            System.out.println(result.longitude + "," + result.latitude);
        }
    }
}
