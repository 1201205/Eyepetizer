package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ray on 16/8/28.
 */
public class PullToRefreshView extends ViewGroup {
    private ImageView mNoNetImage;
    private TextView mTimeView;


    public PullToRefreshView(Context context) {
        this(context, null);
    }


    public PullToRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
