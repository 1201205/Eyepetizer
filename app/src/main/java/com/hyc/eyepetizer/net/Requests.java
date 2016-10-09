package com.hyc.eyepetizer.net;

import android.text.TextUtils;
import android.util.Log;


import com.hyc.eyepetizer.MainApplication;
import com.hyc.eyepetizer.utils.AppUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ray on 16/5/5.
 */
public class Requests {
    private static Api sApi = null;
    private static Object sObject = new Object();
    private static String TAG = "request";
    private static Interceptor sInterceptor;
    private static File sHttpCacheDirectory;
    private static Cache sCache;
    private static OkHttpClient sClient;
    private static String UUID= "8954dd2dac7e41d68d967d5cc8115ced8b7af94c";

    public static Api getApi() {
        synchronized (sObject) {
            if (sApi == null) {
                if (sInterceptor == null) {
                    init();
                }
                sApi = new Retrofit.Builder().baseUrl("http://baobab.wandoujia.com/").client(sClient)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create()).build().create(Api.class);
            }
            return sApi;
        }
    }

    private static void init() {
        sInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.v(TAG, "request:" + request.toString());
                long t1 = System.nanoTime();
                //udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=22
                //build=423001&channel=xiaomi&mobi_app=android&platform=android&screen=xxhdpi&ts=1471273896000&sign=fbdcfe141853f7e2c84c4d401f6a8758
                HttpUrl u=request.url().newBuilder().addQueryParameter("udid",UUID).addQueryParameter("vn","2.4.1")
                        .addQueryParameter("deviceModel",android.os.Build.MODEL).addQueryParameter("first_channel","eyepetizer_xiaomi_market")
                        .addQueryParameter("last_channel","eyepetizer_xiaomi_market").addQueryParameter("system_version_code",android.os.Build.VERSION.SDK).build();

                String[] s=u.url().toString().split("\\?");
                Log.e("request",s[1]);
                request=request.newBuilder().url(u).build();
                Log.e("request",request.toString());
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                Log.v(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=60";
                }
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        };
        //设置缓存路径
        sHttpCacheDirectory = new File(MainApplication.getApplication().getCacheDir(), "responses");
        //设置缓存 20M
        sCache = new Cache(sHttpCacheDirectory, 20 * 1024 * 1024);
        sClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(sInterceptor)
                .cache(sCache).build();
    }
}
