package com.hyc.eyepetizer.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.hyc.eyepetizer.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/8/29.
 */
public class PullToRefreshView extends FrameLayout {
    private View mHead;
    private TextView mRemainTime;
    private View mLoadingView;
    private boolean mHasHead;
    private RecyclerView mTarget;
    private LinearLayoutManager mManager;
    private boolean mCanScroll;
    private float mCurrentX;
    private float mCurrentY;
    private int mHeadHeight;
    private int mAnimHeight;
    private Animator mScrollAnimator;
    private Scroller mScroller;

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
        mHead = findViewById(R.id.head);
        if (mHead != null) {
            mHasHead = true;
            mLoadingView = findViewById(R.id.loading);
            mRemainTime = (TextView) findViewById(R.id.tv_remain);
            mTarget= (RecyclerView) findViewById(R.id.target);
        } else {
            mHasHead=false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mAnimHeight= mRemainTime.getBottom();
        mHeadHeight=mHead.getMeasuredHeight();
        Log.e("----","mAnimHeight+++"+mAnimHeight+"++++mHeadHeight++++"+mHeadHeight);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float y=ev.getRawY();

        boolean resume=false;
        if (mManager==null) {
            mManager=(LinearLayoutManager) mTarget.getLayoutManager();
        }
        if (mManager.findFirstCompletelyVisibleItemPosition() == 0) {
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
                Log.e("----","y--"+y+"--deltaY--"+deltaY+"--getScrollY--"+getScrollY());
                if (mCanScroll) {
                    if (deltaY>0) {
                        resume=true;
                        if (Math.abs(getScrollY())<mHeadHeight) {
                            scrollBy(0, (int) -deltaY*2/3);
                        }
                    } else if (getScrollY()<0) {
                        resume=true;
                        if ((getScrollY() - deltaY * 2 / 3) > 0) {
                            scrollTo(0, 0);
                        } else {
                            scrollBy(0, (int) -deltaY*2/3);
                        }
                    }
                }
                mCurrentY=y;
                if (resume) {
                    return resume;
                } else {
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (Math.abs(getScrollY())>(mHeadHeight-mAnimHeight)) {
                    startScroll(Math.abs(getScrollY())-(mHeadHeight-mAnimHeight));
                }else
                if ((mHeadHeight-mAnimHeight)/2<Math.abs(getScrollY())) {
                    startScroll(-(mHeadHeight-mAnimHeight-Math.abs(getScrollY())));
                } else if ((mHeadHeight-mAnimHeight)/2>Math.abs(getScrollY())) {
                    startScroll((Math.abs(getScrollY())));
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
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
        Log.e("test1",scroll+"---");
        mScroller.startScroll(getScrollX(),getScrollY(),getScrollX(), (int) scroll,(int) Math.abs(scroll)*4);
        invalidate();
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            Log.e("mScroller",mScroller.getCurrY()+"---");
            postInvalidate();
        }
        super.computeScroll();
    }
}
