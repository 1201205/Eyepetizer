package com.hyc.eyepetizer.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hyc.eyepetizer.MainApplication;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/26.
 */
public class AppUtil {
    /**
     * 生成唯一设备识别id
     * @return
     */
    public static String getAndroidUUID() {
        final TelephonyManager tm = (TelephonyManager) MainApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(MainApplication.getApplication().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString().replace("-", "");
        Log.d("debug", "uuid=" + uniqueId);
        return uniqueId;
    }
    /**
     *
     * dp转px
     * @param dpValue
     * @return
     */
    public static float dip2px(float dpValue) {
        final float scale = MainApplication.getApplication().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param pxValue
     * @return
     */
    public static float px2dip(float pxValue) {
        final float scale = MainApplication.getApplication().getResources().getDisplayMetrics().density;
        return  (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        return mScreenWidth;
    }


    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenHeight = dm.heightPixels;
        return mScreenHeight;
    }

    public static int getStatusHeight(Activity activity) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }
}
