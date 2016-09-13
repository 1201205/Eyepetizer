package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.AppUtil;

/**
 */
public class MyNestedScrollView extends NestedScrollView {
    float mCurrentY;
    private int downX;
    private int downY;
    private int mTouchSlop;
    private int mHeight;
    private int mWidth;
    private View head;
    private View indicator;
    private ViewPager pager;
    private View all;
    private float mTitleBarHeight;
    private int mHintY;


    public MyNestedScrollView(Context context) {
        super(context);
        mTitleBarHeight = AppUtil.dip2px(45);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitleBarHeight = AppUtil.dip2px(45);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTitleBarHeight = AppUtil.dip2px(45);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHintY = head.getMeasuredHeight() - indicator.getMeasuredHeight();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mHeight = height;
        mWidth = width;
        //FrameLayout.LayoutParams params= (LayoutParams) pager.getLayoutParams();
        //params.width=mWidth;
        //params.height=mHeight-mHeight-head.getMeasuredHeight();
        setMeasuredDimension(width, height);
        //        measureChild(all,MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
        //                MeasureSpec.makeMeasureSpec(height+mHintY,
        //                        MeasureSpec.AT_MOST));
        all.measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(height + mHintY,
                MeasureSpec.AT_MOST));
        pager.measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(height - indicator.getMeasuredHeight(),
                MeasureSpec.AT_MOST));
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        pager.layout(0, head.getMeasuredHeight(), mWidth,
            (mHeight + mHintY));
        //
        //        pager.postDelayed(new Runnable() {
        //            @Override public void run() {
        //                scrollTo(0, mHintY);
        //            }
        //        }, 1000);
        Log.e("hyc-test", "head--" + head.getMeasuredWidth() + "---" + head.getMeasuredHeight());
        Log.e("hyc-test", "pager--" + pager.getMeasuredWidth() + "---" + pager.getMeasuredHeight());
        Log.e("hyc-test", "pageall--" + mWidth + "---" + mHeight);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        //pager.layout(0,head.getHeight(),mWidth,mHeight-head.getWidth());
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        head = findViewById(R.id.rl_head);
        indicator = findViewById(R.id.rl_indicator);
        pager = (ViewPager) findViewById(R.id.vp_target);
        all = findViewById(R.id.fl_all);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float y = event.getRawY();
        int scrollY = getScrollY();
        boolean intercept = false;
        if (scrollY < mHintY) {
            intercept = true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mCurrentY = y;
            case MotionEvent.ACTION_MOVE:

                if (intercept) {
                    if (scrollY == 0 && y > mCurrentY) {
                        mCurrentY = y;
                        return super.dispatchTouchEvent(event);
                    } else if (scrollY + mCurrentY - y < 0) {
                        mCurrentY = y;
                        return true;
                    } else if (mCurrentY - y + scrollY > mHintY) {
                        mCurrentY = y;
                        return true;
                    } else {
                        mCurrentY = y;
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
