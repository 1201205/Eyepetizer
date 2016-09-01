package com.hyc.eyepetizer.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.hyc.eyepetizer.MainApplication;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/31.
 */
public class TypefaceHelper {
    public static final String NORMAL="normal";
    public static final String BOLD="bold";
    public static final String TEMP="temp";
    private static final HashMap<String,Typeface> sTypefaceMap=new HashMap<>();

    public static Typeface getTypeface(String type){
        if (sTypefaceMap.get(type) != null) {
            return sTypefaceMap.get(type);
        } else {
            Typeface typeface=getTypefaceInternal(type);
            sTypefaceMap.put(type,typeface);
            return typeface;
        }
    }

    private static Typeface getTypefaceInternal(String type){
        AssetManager manager= MainApplication.getApplication().getAssets();
        switch (type){
            case BOLD:
                return Typeface.createFromAsset(manager, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF");//根据路径得到Typeface
            case NORMAL:
                return Typeface.createFromAsset(manager, "fonts/FZLanTingHeiS-L-GB-Regular.TTF");//根据路径得到Typeface
            case TEMP:
                return Typeface.createFromAsset(manager, "fonts/Lobster-1.4.otf");//根据路径得到Typeface
        }
        return Typeface.DEFAULT;
    }
}
