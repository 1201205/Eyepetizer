package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.hyc.eyepetizer.R;
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
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String type = array.getString(R.styleable.CustomTextView_typeface);
        array.recycle();
        if (TextUtils.isEmpty(type)) {
            type = TypefaceHelper.NORMAL;
        }
        setTypeface(TypefaceHelper.getTypeface(type));
    }
}
