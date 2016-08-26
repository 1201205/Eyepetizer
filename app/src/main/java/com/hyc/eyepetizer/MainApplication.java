package com.hyc.eyepetizer;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MainApplication extends Application {
    private static Context sContext;
    private static final Object sLock=new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext=this;
        Fresco.initialize(this);
    }

    public static Context getApplication() {
        synchronized (sLock){
            return sContext;
        }
    }

}
