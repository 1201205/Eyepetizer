package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.AppUtil;

/**
 * Created by ray on 16/9/12.
 */
public class PullToScrollView extends ScrollView {
    private int mHeight;
    private int mWidth;
    private View head;
    private View indicator;
    private ViewPager pager;
    private float mTitleBarHeight;
    private int mHintY;
    private GestureDetector mGestureDetector;
    private Scroller mScroller;



    public PullToScrollView(Context context) {
        this(context, null);
    }


    public PullToScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PullToScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTitleBarHeight = AppUtil.dip2px(45);
//        mGestureDetector=new GestureDetector(getContext(),new GestureListener());
//        mScroller = new Scroller(getContext());
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    private float mCurrentY;
    int mPointerId;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getRawY();
        int scrollY = getScrollY();
        boolean intercept = false;
        if (scrollY < mHintY) {
            intercept = true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPointerId = event.getPointerId(0);

                mVelocityTracker = VelocityTracker.obtain(); // 初始化
                mVelocityTracker.addMovement(event);
                mCurrentY = y;
            case MotionEvent.ACTION_MOVE:

                if (intercept) {
                    if (scrollY == 0 && y > mCurrentY) {
                        mCurrentY = y;
                        return super.dispatchTouchEvent(event);
                    } else if (scrollY+mCurrentY-y<0) {
                        mCurrentY=y;
                        scrollTo(0,0);
                        return true;
                    } else if (mCurrentY - y + scrollY > mHintY) {
                        scrollBy(0, mHintY - scrollY);
                    } else {
                        scrollBy(0, (int) (mCurrentY - y));
                    }
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) mVelocityTracker.getYVelocity(mPointerId);
                    // 在 ACTION_UP 和 ACTION_CANCEL 中 销毁
                    if (initialVelocity!=0) {
                        fling(initialVelocity);
                    }

                    mCurrentY = y;
                    return true;
                }
                break;
            //https://github.com/hyr0318/MaterialCoordinatorLayout
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity(mPointerId);
                mVelocityTracker.recycle();
                Log.e("initialVelocity",initialVelocity+"----");
                // 在 ACTION_UP 和 ACTION_CANCEL 中 销毁
                fling(initialVelocity);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    VelocityTracker mVelocityTracker;
    int mMaximumVelocity ;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHintY = head.getMeasuredHeight() - indicator.getMeasuredHeight();
        int width=  MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        mHeight = height;
        mWidth = width;
        //FrameLayout.LayoutParams params= (LayoutParams) pager.getLayoutParams();
        //params.width=mWidth;
        //params.height=mHeight-mHeight-head.getMeasuredHeight();
        setMeasuredDimension(width,height);
//        measureChild(all,MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
//                MeasureSpec.makeMeasureSpec(height+mHintY,
//                        MeasureSpec.AT_MOST));
        all.measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(height+mHintY,
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
        all=findViewById(R.id.fl_all);
    }
    private View all;
//    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        @Override
//        public boolean onDown(MotionEvent motionEvent) {
//            Log.e("--GestureListener--","onDown");
//            return true;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            Log.e("--GestureListener--","onScroll");
//            int scrollY = getScrollY();
//            boolean intercept = true;
//            if (scrollY > mHintY) {
//               return false;
//            } else if (getScrollY() > mTitleBarHeight) {
//
//            }
//            Log.e("--motion--",v1+"+++1++"+motionEvent.getRawY());
//            float dY=-v1;
//            Log.e("---",dY+"-");
//            if (scrollY == 0 && dY>0) {
//                mCurrentY=motionEvent1.getRawY();
//                        return false;
//                    } else if (scrollY-dY<0) {
//                        mCurrentY=motionEvent1.getRawY();
//                        scrollTo(0,0);
//                        return true;
//                    } else if (scrollY-dY > mHintY) {
//                        scrollBy(0, mHintY - scrollY);
//                    } else {
//                        scrollBy(0, (int) (-dY));
//                    }
//            return false;
//        }
//        @Override
//        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            Log.e("--GestureListener--","onFling");
//
//            mScroller.fling(0,getScrollY(),0, -(int) v1*3,0,0,0,mHintY);
//            return false;
//        }
//    }
//    @Override
//    public void computeScroll() {
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            postInvalidate();
//        }
//        super.computeScroll();
//    }

}
