package com.sf.DarkCalculator;

import android.util.Log;

public class LogUtil {
    public static boolean DEBUG = true;

    /**
     * debug标签的头部
     */
    private static final String TAG = "nanfang";
    /**
     * 打印http请求
     */
    public static final String TAG_HTTP = "http";
    /**
     * 错误和异常的tag
     */
    public static final String TAG_ERROR = "error";

    public static void d(String tag, Object obj) {
        if (DEBUG) {
          Log.d(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()));
        }
    }
    public static void v(String tag, Object obj){
        if (DEBUG) {
           Log.v(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()));
        }
    }

    public static void i(String tag, Object obj){
        if (DEBUG) {
            Log.i(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()));
        }
    }

    public static void w(String tag, Object obj){
        if (DEBUG) {
            Log.w(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()));
        }
    }

    public static void w(String tag, Object obj, Throwable tr){
        if (DEBUG) {
            Log.w(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()),tr);
        }
    }

    public static void e(String tag, Object obj){
        if (DEBUG) {
            Log.e(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()));
        }
    }
    public static void e(Object obj){
            Log.e(TAG, obj.toString() );
    }

    public static void e(String tag, Object obj, Throwable tr){
        if (DEBUG) {
            Log.e(TAG, tag +"-->" + (obj == null ? "null" : obj.toString()),tr);
        }
    }


}
