package com.sf.DarkCalculator;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by xilinch on 2017/4/11.
 * 封装对integer的操作
 */

public class UtilInteger {

    /**
     * string到int的转化
     * 缺省值为-1
     * @param parseStr
     * @return
     */
    public static int parseInt(String parseStr){
        int defaultValue = -1;
        return parseInt(parseStr,defaultValue);
    }

    /**
     * string到int的转化
     * @param parseStr
     * @return
     */
    public static int parseInt(String parseStr, int defaultValue){
        int result = defaultValue;
        if(!TextUtils.isEmpty(parseStr)){
            try{
                result = Integer.parseInt(parseStr);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return result;
    }

    /**
     * string到long的转化
     * 缺省值为-1
     * @param parseStr
     * @return
     */
    public static long parseLong(String parseStr){
        long defaultValue = -1;
        return parseLong(parseStr,defaultValue);
    }

    /**
     * string到long的转化
     * @param parseStr
     * @return
     */
    public static long parseLong(String parseStr, long defaultValue){
        long result = defaultValue;
        if(!TextUtils.isEmpty(parseStr)){
            try{
                result = Long.parseLong(parseStr);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
       return (int) (dpValue * scale + 0.5f);
    }

    /**
    *根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
