package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/9/2.
 */
public class CustomDraweeView extends SimpleDraweeView {


    public CustomDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }
    public CustomDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CustomDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Drawable drawable = getTopLevelDrawable();
        if (drawable != null) {
            drawable.setBounds(0, 0, w, h);
        }
    }
}
