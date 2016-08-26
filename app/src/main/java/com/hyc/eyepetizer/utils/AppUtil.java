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
}
