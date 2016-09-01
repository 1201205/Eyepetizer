package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hyc.eyepetizer.utils.TypefaceHelper;

/**
 * Created by Administrator on 2016/9/1.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.NORMAL));
    }
}
