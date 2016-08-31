package com.hyc.eyepetizer.utils;

import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/8/31.
 */
public class FrescoHelper {
    /**
     * fresco 加载图片
     * @param view
     * @param url
     */
    public static void loadUrl(SimpleDraweeView view,String url){
        if (!TextUtils.isEmpty(url)) {
            view.setImageURI(url);
        }
    }
}
