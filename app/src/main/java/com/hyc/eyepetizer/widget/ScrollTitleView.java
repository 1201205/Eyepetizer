package com.hyc.eyepetizer.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ScrollTitleView extends LinearLayout{
    private Scroller mScroller;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private ViewPager mViewPager;
    private View mHeadView;
    private int mMaxY;
    private int mHeadHeight;
    private DIRECTION mDirection;
    enum DIRECTION {
        UP,
        DOWN
    }

    public ScrollTitleView(Context context) {
        this(context,null);
    }

    public ScrollTitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller=new Scroller(context);
        setOrientation(VERTICAL);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount()<1) {
            throw new RuntimeException("no child in ScrollTitleView");
        }
        //规定只能在第一个
        mHeadView=getChildAt(0);
        mMaxY=mHeadHeight=mHeadView.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + mMaxY, MeasureSpec.EXACTLY));

//        measureChildWithMargins(mHeadView,widthMeasureSpec,);
    }
    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt instanceof ViewPager) {
                mViewPager = (ViewPager) childAt;
            }
        }
        super.onFinishInflate();
    }
    private float mLastX;
    private float mLastY;
    private boolean notIntercept;
    private boolean firstMove;
    private VelocityTracker mVelocityTracker;
    private int mLastScrollerY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX=ev.getX();
        float currentY=ev.getY();
        int dx= (int) Math.abs(currentX-mLastX);
        int dy= (int) Math.abs(currentY-mLastY);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=currentX;
                mLastY=currentY;
                firstMove=true;
                notIntercept=false;
                initOrResetVelocityTracker();
                break;
            case MotionEvent.ACTION_MOVE:
                //未到达mTouchSlop直接返回
                if (firstMove) {
                    if (dx > mTouchSlop || dy > mTouchSlop) {
                        if (dx < dy) {
                            notIntercept = false;
                        } else {
                            notIntercept = true;
                        }
                        firstMove = false;
                    } else {
                        Log.e("hyc-ui","not handle");
                        return super.dispatchTouchEvent(ev);
                    }

                }
                //横向事件，不处理
                if (notIntercept) {
                    Log.e("hyc-ui","do notIntercept ");
                    return super.dispatchTouchEvent(ev);
                }
                Log.e("hyc-ui","handle");
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(ev);
                //
                if (getScrollY()<mHeadHeight&&isTop()) {
                    Log.e("hyc-ui",getScrollY()+"+++++++"+mHeadHeight);
                    mViewPager.requestDisallowInterceptTouchEvent(true);
                    scrollBy(0, (int) (dy + 0.5));
                }
                mLastX=currentX;
                mLastY=currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (!notIntercept) {
                    mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);
                    float yVelocity = -mVelocityTracker.getYVelocity();
                    if (Math.abs(yVelocity)>mMinimumVelocity) {
                        mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
                        if (getScrollY()==mHeadHeight) {
                            mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            mScroller.computeScrollOffset();
                            mLastScrollerY = getScrollY();
                            invalidate();
                        }
                    }
                }
                mLastX=currentX;
                mLastY=currentY;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    private int mMinY ;
    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currentY=mScroller.getCurrY();
            if (mDirection==DIRECTION.UP) {
                if (getScrollY() >= mHeadHeight) {
//                    int distance=mScroller.getFinalY()-currentY;
//                    int duration=mScroller.getDuration()-mScroller.timePassed();
                    mContainer.getScrollableView().fling(0, (int) mScroller.getCurrVelocity());
                } else {
                    scrollTo(0, currentY);
                }
            }else {
                if (isTop()) {
                    int deltaY = (currentY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    scrollTo(0, toY);
                    if (currentY <= mMinY) {
                        mScroller.forceFinished(true);
                        return;
                    }
                }
                invalidate();
            }
            mLastScrollerY = currentY;
        }
    }

    public void setContainer(ScrollableContainer container) {
        mContainer = container;
    }
    private boolean isTop() {
        RecyclerView recyclerView=mContainer.getScrollableView();
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null) {
                    return true;
                }
                if (firstVisibleItemPosition == 0) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                    int topMargin = lp.topMargin;
                    int top = childAt.getTop();
                    if (top >= topMargin) {
                        return true;
                    }
                }
            }else {
                throw new IllegalArgumentException("only support LinearLayoutManager");
            }
        }
        return false;
    }

    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= mHeadHeight) {
            toY = mHeadHeight;
        } else if (toY <= mMinY) {
            toY = mMinY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y >= mMaxY) {
            y = mMaxY;
        } else if (y <= mMinY) {
            y = mMinY;
        }
        super.scrollTo(x, y);
    }
    private ScrollableContainer mContainer;
    public interface ScrollableContainer{
        RecyclerView getScrollableView();
    }
}
