package com.hyc.eyepetizer.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hyc.eyepetizer.MainApplication;

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
}
