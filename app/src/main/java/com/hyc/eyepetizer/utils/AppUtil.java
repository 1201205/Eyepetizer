package com.hyc.eyepetizer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.hyc.eyepetizer.MainApplication;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/26.
 */
public class AppUtil {

    private static int sStausHeight = -1;
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
    protected static final String PREFS_FILE = "gank_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "gank_device_id";
    protected static String uuid;
    static public String getUDID()
    {
        if( uuid ==null ) {
            synchronized (AppUtil.class) {
                if( uuid == null) {
                    final SharedPreferences prefs = MainApplication.getApplication().getSharedPreferences( PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null );

                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = id;
                    } else {

                        final String androidId = Settings.Secure.getString(MainApplication.getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                            } else {
                                final String deviceId = ((TelephonyManager) MainApplication.getApplication().getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                    }
                }
            }
        }
        uuid = uuid.replace("-", "");
        return uuid;
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


    public static int getStatusBarHeight(Context context) {
        if (sStausHeight == -1) {
            int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStausHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return sStausHeight;
    }


    public static void showToast(String s) {
        Toast.makeText(MainApplication.getApplication(), s, Toast.LENGTH_LONG).show();
    }


    public static void showToast(int id) {
        Toast.makeText(MainApplication.getApplication(), id, Toast.LENGTH_LONG).show();
    }


    public static int getColor(int id) {
        return MainApplication.getApplication().getResources().getColor(id);
    }

    public static String getString(int id) {
        return MainApplication.getApplication().getResources().getString(id);
    }
}
