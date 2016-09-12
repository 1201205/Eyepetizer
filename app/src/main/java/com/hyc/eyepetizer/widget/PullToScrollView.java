package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.hyc.eyepetizer.R;

/**
 * Created by ray on 16/9/12.
 */
public class PullToScrollView extends FrameLayout {
    private int mHeight;
    private int mWidth;
    private View head;
    private View indicator;
    private ViewPager pager;


    public PullToScrollView(Context context) {
        this(context, null);
    }


    public PullToScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PullToScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ;
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        //FrameLayout.LayoutParams params= (LayoutParams) pager.getLayoutParams();
        //params.width=mWidth;
        //params.height=mHeight-mHeight-head.getMeasuredHeight();

        measureChild(pager, MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(mHeight - indicator.getMeasuredHeight(),
                MeasureSpec.AT_MOST));
    }


    @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        pager.layout(0, head.getMeasuredHeight(), mWidth,
            (mHeight - indicator.getMeasuredHeight() + head.getMeasuredHeight()));

        pager.postDelayed(new Runnable() {
            @Override public void run() {
                scrollTo(0, head.getMeasuredHeight() - indicator.getMeasuredHeight());
            }
        }, 1000);
        Log.e("hyc-test", "head--" + head.getMeasuredWidth() + "---" + head.getMeasuredHeight());
        Log.e("hyc-test", "pager--" + pager.getMeasuredWidth() + "---" + pager.getMeasuredHeight());
        Log.e("hyc-test", "pageall--" + mWidth + "---" + mHeight);

    }


    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        //pager.layout(0,head.getHeight(),mWidth,mHeight-head.getWidth());
    }


    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        head = findViewById(R.id.rl_head);
        indicator = findViewById(R.id.rl_indicator);
        pager = (ViewPager) findViewById(R.id.vp_target);

    }
}
