package com.hyc.eyepetizer.widget;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.AppUtil;

/**
 * Created by Administrator on 2016/8/29.
 */
public class PullToRefreshView extends FrameLayout {
    @BindView(R.id.head)
    View mHead;
    @BindView(R.id.tv_remain)
    TextView mRemainTime;
    @BindView(R.id.loading)
    LoadingAnimView mLoadingView;
    @BindView(R.id.target)
    RecyclerView mTarget;
    private boolean mHasHead;
    private LinearLayoutManager mManager;
    private boolean mCanScroll;
    private float mCurrentX;
    private float mCurrentY;
    private int mHeadHeight;
    private int mAnimHeight;
    private Animator mScrollAnimator;
    private Scroller mScroller;
    private int mTextHeight;

    public PullToRefreshView(Context context) {
        this(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count <= 0) {
            return;
        }
        ButterKnife.bind(this);
        //mHead = findViewById(R.id.head);
        //if (mHead != null) {
        //    mHasHead = true;
        //    mLoadingView = (LoadingAnimView) findViewById(R.id.loading);
        //    mRemainTime = (TextView) findViewById(R.id.tv_remain);
        //    mTarget= (RecyclerView) findViewById(R.id.target);
        //} else {
        //    mHasHead=false;
        //}
        
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mAnimHeight= mRemainTime.getBottom();
        mHeadHeight=mHead.getMeasuredHeight();
        mTextHeight = mRemainTime.getHeight();
        //Log.e("----","mAnimHeight+++"+mAnimHeight+"++++mHeadHeight++++"+mHeadHeight);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float y=ev.getRawY();

        boolean resume=false;
        if (mManager==null) {
            mManager=(LinearLayoutManager) mTarget.getLayoutManager();
        }
        if (mManager.findFirstCompletelyVisibleItemPosition() == 0 && !mLoadingView.isLoading()) {
            mCanScroll = true;
        } else {
            mCanScroll=false;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mCurrentY=y;
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mCurrentY;
                //Log.e("----","y--"+y+"--deltaY--"+deltaY+"--getScrollY--"+getScrollY());
                if (mCanScroll) {
                    int scroll = (int) -deltaY * 2 / 3;
                    if (deltaY>0) {
                        resume=true;
                        if (Math.abs(getScrollY())<mHeadHeight) {
                            scrollBy(0, scroll);
                        }
                    } else if (getScrollY()<0) {
                        resume=true;
                        if ((getScrollY() - deltaY * 2 / 3) > 0) {
                            scrollTo(0, 0);
                        } else {
                            scrollBy(0, scroll);
                        }
                    }
                    alphaAnimate();
                    round(scroll);
                }
                mCurrentY=y;
                if (resume) {
                    return resume;
                } else {
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (mCanScroll) {
                    if (-getScrollY() > (mHeadHeight - mAnimHeight)) {
                        startScroll(Math.abs(getScrollY()) - (mHeadHeight - mAnimHeight));
                        mLoadingView.startAnim();
                    } else if ((mHeadHeight - mAnimHeight) / 2 < -getScrollY()) {
                        startScroll(-(mHeadHeight - mAnimHeight - Math.abs(getScrollY())));
                        mLoadingView.startAnim();
                    } else if ((mHeadHeight - mAnimHeight) / 2 > -getScrollY()) {
                        startScroll((Math.abs(getScrollY())));
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private void round(int scroll) {
        mLoadingView.round(scroll);
    }


    private void alphaAnimate() {
        if (getScrollY() < -mAnimHeight) {
            float x = ((Math.abs(getScrollY()) - mAnimHeight)) * 1f /
                (mHeadHeight - mAnimHeight - AppUtil.dip2px(10));
            //Log.e("XXXX",x+"");
            mRemainTime.setAlpha(x);
        }
    }


    //    private void startScroll(float scroll){
//        mScrollAnimator=ObjectAnimator.ofFloat(this,"y",scroll);
//        mScrollAnimator.setDuration((long) Math.abs(scroll)*5);
//        mScrollAnimator.setInterpolator(new DecelerateInterpolator());
//        mScrollAnimator.start();
//
//    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    private void startScroll(float scroll){
        //Log.e("test1",scroll+"---");
        mScroller.startScroll(getScrollX(),getScrollY(),getScrollX(), (int) scroll,(int) Math.abs(scroll)*4);
        invalidate();
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //Log.e("mScroller",mScroller.getCurrY()+"---");
            alphaAnimate();
            postInvalidate();
        }
        super.computeScroll();
    }


    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
