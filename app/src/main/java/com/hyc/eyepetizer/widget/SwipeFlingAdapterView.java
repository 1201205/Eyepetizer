package com.hyc.eyepetizer.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Adapter;
import android.widget.FrameLayout;
import com.hyc.eyepetizer.R;
import java.util.ArrayList;

/**
 * Created by dionysis_lorentzos on 5/8/14
 * for package com.lorentzos.swipecards
 * and project Swipe cards.
 * Use with caution dinosaurs might appear!
 */

public class SwipeFlingAdapterView extends BaseFlingAdapterView {

    private static final float SCALE_STEP = 0.08f; // view叠加缩放的步长
    // 支持左右滑
    public boolean isNeedSwipe = true;
    private ArrayList<View> cacheItems = new ArrayList<>();
    //缩放层叠效果
    //缩放层叠效果
    private int yOffsetStep; // view叠加垂直偏移量的步长
    private int MAX_VISIBLE = 4; // 值建议最小为4
    private int MIN_ADAPTER_STACK = 6;
    private float ROTATION_DEGREES = 2f;
    private int LAST_OBJECT_IN_STACK = 0;
    private Adapter mAdapter;
    private onFlingListener mFlingListener;
    private AdapterDataSetObserver mDataSetObserver;
    private boolean mInLayout = false;
    private View mActiveCard = null;
    private OnItemClickListener mOnItemClickListener;
    private FlingCardListener flingCardListener;
    private int initTop;
    private int initLeft;
    private int mSlop;
    private View mAddView;
    private float startScale;
    private int removeCount;
    private float x = -1;
    private float y;
    private float lastX;
    private float lastY;
    private boolean forLeft;
    private boolean forRight;
    private int mHintOffset;
    private boolean forAdd;
    private boolean forRemove;
    private float mAddViewX;
    private float mAddViewY;


    public SwipeFlingAdapterView(Context context) {
        this(context, null);
    }


    public SwipeFlingAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SwipeFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeFlingAdapterView,
            defStyle, 0);
        MAX_VISIBLE = a.getInt(R.styleable.SwipeFlingAdapterView_max_visible, MAX_VISIBLE);
        MIN_ADAPTER_STACK = a.getInt(R.styleable.SwipeFlingAdapterView_min_adapter_stack,
            MIN_ADAPTER_STACK);
        ROTATION_DEGREES = a.getFloat(R.styleable.SwipeFlingAdapterView_rotation_degrees,
            ROTATION_DEGREES);
        yOffsetStep = a.getDimensionPixelOffset(R.styleable.SwipeFlingAdapterView_y_offset_step, 0);
        a.recycle();
        mSlop = ViewConfiguration.getTouchSlop();
    }


    public void setIsNeedSwipe(boolean isNeedSwipe) {
        this.isNeedSwipe = isNeedSwipe;
    }


    public int[] getWidthAndHeight() {
        int count = getChildCount() - 1;
        if (getChildCount() >= 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getChildAt(
                count).getLayoutParams();
            return new int[] { getChildAt(count).getWidth(), getChildAt(count).getHeight() };
        }
        return new int[] { 0, 0 };
    }


    /**
     * A shortcut method to set both the listeners and the adapter.
     *
     * @param context The activity context which extends onFlingListener, OnItemClickListener or
     * both
     * @param mAdapter The adapter you have to set.
     */
    public void init(final Context context, Adapter mAdapter) {
        if (context instanceof onFlingListener) {
            mFlingListener = (onFlingListener) context;
        } else {
            throw new RuntimeException(
                "Activity does not implement SwipeFlingAdapterView.onFlingListener");
        }
        if (context instanceof OnItemClickListener) {
            mOnItemClickListener = (OnItemClickListener) context;
        }
        setAdapter(mAdapter);
    }


    @Override
    public View getSelectedView() {
        return mActiveCard;
    }


    public void setAddInLayout(){
        forAdd=true;
        forRemove=false;
    }


    public void setRemoveInLayout(){
        forAdd=false;
        forRemove=true;
    }


    public void reset(){
        forAdd=false;
        forRemove=false;
    }


    @Override
    public void requestLayout() {
        if (!mInLayout) {
//            if (forAdd) {
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mAddView.getLayoutParams();
//                addViewInLayout(mAddView, 0, lp, true);
//            }
////            if (forRemove) {
////            }
//            reset();
//            if (getChildCount()>0) {
//                removeAllViewsInLayout();
//            }
            super.requestLayout();
        }
    }


    public void removeFirst(){
        final int adapterCount = mAdapter.getCount();
        if (adapterCount == 0) {
            //            removeAllViewsInLayout();
            removeAndAddToCache(0);
        } else {
            View topCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null && topCard != null && topCard == mActiveCard) {
                //                removeViewsInLayout(0, LAST_OBJECT_IN_STACK);
                removeAndAddToCache(1);
                layoutChildren(1, adapterCount);
//                layoutFirst();
            } else {
                // Reset the UI and set top view listener
                //                removeAllViewsInLayout();
                removeAndAddToCache(0);
                layoutChildren(0, adapterCount);
                setTopView();
            }
        }
    }


    public void addFirst(){
        View item = null;
        if (cacheItems.size() > 0) {
            item = cacheItems.get(0);
            cacheItems.remove(item);
        }
        View newUnderChild = mAdapter.getView(0, item, this);
        if (newUnderChild.getVisibility() != GONE) {
            if (getChildCount() >= MAX_VISIBLE) {
                removeViewInLayout(getChildAt(0));
                makeAndAddView(newUnderChild, 0, MAX_VISIBLE - 1);
            } else {
                makeAndAddView(newUnderChild, 0, getChildCount());
                if (LAST_OBJECT_IN_STACK+1<MAX_VISIBLE) {
                    LAST_OBJECT_IN_STACK++;
                }
            }
        }
        setTopView();
    }


    public void setAddView(View view, int hintOffset) {
        mAddView = view;
        mHintOffset = hintOffset;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return;
        }
        mInLayout = true;
        final int adapterCount = mAdapter.getCount();
        if (adapterCount == 0) {
            //            removeAllViewsInLayout();
            removeAndAddToCache(0);
        } else {
            View topCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null && topCard != null && topCard == mActiveCard) {
                //                removeViewsInLayout(0, LAST_OBJECT_IN_STACK);
                removeAndAddToCache(1);
                layoutChildren(1, adapterCount);
//                layoutFirst();
            } else {
                // Reset the UI and set top view listener
                //                removeAllViewsInLayout();
                removeAndAddToCache(0);
                layoutChildren(0, adapterCount);
                setTopView();
            }
        }
        mInLayout = false;

        if (initTop == 0 && initLeft == 0 && mActiveCard != null) {
            initTop = mActiveCard.getTop();
            initLeft = mActiveCard.getLeft();
        }

        if (adapterCount < MIN_ADAPTER_STACK) {
            if (mFlingListener != null) {
                mFlingListener.onAdapterAboutToEmpty(adapterCount);
            }
        }
    }


    private void removeAndAddToCache(int remain) {
        View view;

        if (!forRemove) {
            for (int i = 0; i < getChildCount() - remain; ) {
                view = getChildAt(i);
                removeViewInLayout(view);
                cacheItems.add(view);
            }
        }

    }


    private void layoutChildren(int startingIndex, int adapterCount) {
        if (!forRemove) {
            while (startingIndex < Math.min(adapterCount, MAX_VISIBLE)) {
                View item = null;
                if (cacheItems.size() > 0) {
                    item = cacheItems.get(0);
                    cacheItems.remove(item);
                }
                View newUnderChild = mAdapter.getView(startingIndex, item, this);
                if (newUnderChild.getVisibility() != GONE) {
                    makeAndAddView(newUnderChild, startingIndex);
                    LAST_OBJECT_IN_STACK = startingIndex;
                }

                startingIndex++;
            }
        } else {
            if (adapterCount<MAX_VISIBLE) {
                LAST_OBJECT_IN_STACK--;
                return;
            }
            int x=Math.min(adapterCount, MAX_VISIBLE)-1;
            View item = null;
            if (cacheItems.size() > 0) {
                item = cacheItems.get(0);
                cacheItems.remove(item);
            }
            View newUnderChild = mAdapter.getView(x, item, this);
            if (newUnderChild.getVisibility() != GONE) {
                makeAndAddView(newUnderChild, x);
                LAST_OBJECT_IN_STACK = x;
            }
        }

    }


    private void layoutFirst(){
        View item = null;
        if (cacheItems.size() > 0) {
            item = cacheItems.get(0);
            cacheItems.remove(item);
        }
        View newUnderChild = mAdapter.getView(0, item, this);
        if (newUnderChild.getVisibility() != GONE) {
            makeAndAddView(newUnderChild, 0);
        }
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void makeAndAddView(View child, int index) {

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
        addViewInLayout(child, 0, lp, true);

        final boolean needToMeasure = child.isLayoutRequested();
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(getWidthMeasureSpec(),
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
                lp.width);
            int childHeightSpec = getChildMeasureSpec(getHeightMeasureSpec(),
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin,
                lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }

        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();

        int gravity = lp.gravity;
        if (gravity == -1) {
            gravity = Gravity.TOP | Gravity.START;
        }

        int layoutDirection = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            layoutDirection = getLayoutDirection();
        }
        final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        int childLeft;
        int childTop;
        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                childLeft = (getWidth() + getPaddingLeft() - getPaddingRight() - w) / 2 +
                    lp.leftMargin - lp.rightMargin;
                break;
            case Gravity.END:
                childLeft = getWidth() + getPaddingRight() - w - lp.rightMargin;
                break;
            case Gravity.START:
            default:
                childLeft = getPaddingLeft() + lp.leftMargin;
                break;
        }
        switch (verticalGravity) {
            case Gravity.CENTER_VERTICAL:
                childTop = (getHeight() + getPaddingTop() - getPaddingBottom() - h) / 2 +
                    lp.topMargin - lp.bottomMargin;

                break;
            case Gravity.BOTTOM:
                childTop = getHeight() - getPaddingBottom() - h - lp.bottomMargin;
                break;
            case Gravity.TOP:
            default:
                childTop = getPaddingTop() + lp.topMargin;
                break;
        }
        child.layout(childLeft, childTop, childLeft + w, childTop + h);
        // 缩放层叠效果
        adjustChildView(child, index);
    }


    private void makeAndAddView(View child, int index,int position) {

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
        addViewInLayout(child, position, lp, true);

        final boolean needToMeasure = child.isLayoutRequested();
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(getWidthMeasureSpec(),
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
                    lp.width);
            int childHeightSpec = getChildMeasureSpec(getHeightMeasureSpec(),
                    getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin,
                    lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }

        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();

        int gravity = lp.gravity;
        if (gravity == -1) {
            gravity = Gravity.TOP | Gravity.START;
        }

        int layoutDirection = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            layoutDirection = getLayoutDirection();
        }
        final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        int childLeft;
        int childTop;
        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                childLeft = (getWidth() + getPaddingLeft() - getPaddingRight() - w) / 2 +
                        lp.leftMargin - lp.rightMargin;
                break;
            case Gravity.END:
                childLeft = getWidth() + getPaddingRight() - w - lp.rightMargin;
                break;
            case Gravity.START:
            default:
                childLeft = getPaddingLeft() + lp.leftMargin;
                break;
        }
        switch (verticalGravity) {
            case Gravity.CENTER_VERTICAL:
                childTop = (getHeight() + getPaddingTop() - getPaddingBottom() - h) / 2 +
                        lp.topMargin - lp.bottomMargin;

                break;
            case Gravity.BOTTOM:
                childTop = getHeight() - getPaddingBottom() - h - lp.bottomMargin;
                break;
            case Gravity.TOP:
            default:
                childTop = getPaddingTop() + lp.topMargin;
                break;
        }
        child.layout(childLeft, childTop, childLeft + w, childTop + h);
        // 缩放层叠效果
        adjustChildView(child, index);
    }


    private void adjustChildView(final View child, int index) {
        if (index > -1 && index < MAX_VISIBLE) {
            int multiple;
            if (index > 2) {
                multiple = 2;
            } else {
                multiple = index;
            }
            child.offsetTopAndBottom(yOffsetStep * multiple);
            child.setScaleX(1 - SCALE_STEP * multiple);
            child.setScaleY(1 - SCALE_STEP * multiple);
            if (index == 0 && x == -1) {
                x = child.getX();
                y = child.getY() + mHintOffset;
            }
        }
    }


    private void startValueAnimator(final float value) {
        ValueAnimator animator = ValueAnimator.ofFloat(value, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                adjustChildrenOfUnderTopView((1 - animation.getAnimatedFraction()) * value);
            }
        });
        animator.setDuration(600);
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {

            }


            @Override public void onAnimationEnd(Animator animation) {
                mFlingListener.onReAdd();
                removeCount--;
                if (removeCount < 0) {
                    removeCount = 0;
                }
                forLeft = false;
            }


            @Override public void onAnimationCancel(Animator animation) {

            }


            @Override public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    private void adjustChildrenOfUnderTopView(float scrollRate) {
        int count = getChildCount();
        if (scrollRate == 1) {
            return;
        }
        if (count >= 1) {
            int index;
            int multiple;
            if (count == 2) {
                index = LAST_OBJECT_IN_STACK - 1;
                if (forLeft) {
                    multiple = 2;
                } else {
                    multiple = 1;
                }
            } else {
                index = LAST_OBJECT_IN_STACK - 2;
                multiple = 2;
            }
            float rate = Math.abs(scrollRate);
            int limit;

            if (forLeft) {
                if (count < MAX_VISIBLE - 1) {
                    limit = LAST_OBJECT_IN_STACK + 1;
                } else {
                    index += 1;
                    limit = LAST_OBJECT_IN_STACK + 1;
                }

            } else {
                limit = LAST_OBJECT_IN_STACK;
            }
            if (count==1) {
                index = 0;
                multiple = 1;
            }
            for (; index < limit; index++, multiple--) {
                View underTopView = getChildAt(index);
                int offset = (int) (yOffsetStep * (multiple - rate));
                underTopView.offsetTopAndBottom(offset - underTopView.getTop() + initTop);
                underTopView.setScaleX(1 - SCALE_STEP * multiple + SCALE_STEP * rate);
                underTopView.setScaleY(1 - SCALE_STEP * multiple + SCALE_STEP * rate);
            }
            //if (forLeft) {
            //    i+=1;
            //    for (; i <= LAST_OBJECT_IN_STACK; i++, multiple--) {
            //        View underTopView = getChildAt(i);
            //        int offset = (int) (yOffsetStep * (multiple - rate));
            //        underTopView.offsetTopAndBottom(offset - underTopView.getTop() + initTop);
            //        underTopView.setScaleX(1 - SCALE_STEP * multiple + SCALE_STEP * rate);
            //        underTopView.setScaleY(1 - SCALE_STEP * multiple + SCALE_STEP * rate);
            //    }
            //} else {
            //    for (; i < LAST_OBJECT_IN_STACK; i++, multiple--) {
            //        View underTopView = getChildAt(i);
            //        int offset = (int) (yOffsetStep * (multiple - rate));
            //        underTopView.offsetTopAndBottom(offset - underTopView.getTop() + initTop);
            //        underTopView.setScaleX(1 - SCALE_STEP * multiple + SCALE_STEP * rate);
            //        underTopView.setScaleY(1 - SCALE_STEP * multiple + SCALE_STEP * rate);
            //    }
            //}

        }
    }


    /**
     * Set the top view and add the fling listener
     */
    private void setTopView() {
        if (getChildCount() > 0) {
            //if (getChildCount()==1) {
            //    Debug.startMethodTracing("tracefilename1");
            //}
            mActiveCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null && mFlingListener != null) {
                flingCardListener = new FlingCardListener(mActiveCard, mAdapter.getItem(0),
                    ROTATION_DEGREES, mAdapter.getCount() == 1,
                    new FlingCardListener.FlingListener() {

                        @Override
                        public void onCardExited() {
                            removeViewInLayout(mActiveCard);
                            mActiveCard = null;
                            mFlingListener.removeFirstObjectInAdapter();
                        }


                        @Override
                        public void leftExit(Object dataObject) {
                            mFlingListener.onLeftCardExit(dataObject);
                            removeCount++;
                        }


                        @Override
                        public void rightExit(Object dataObject) {
                            mFlingListener.onRightCardExit(dataObject);
                        }


                        @Override
                        public void onClick(MotionEvent event, View v, Object dataObject) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClicked(event, v, dataObject);
                            }
                        }


                        @Override
                        public void onScroll(float progress, float scrollXProgress) {
                            adjustChildrenOfUnderTopView(progress);
                            mFlingListener.onScroll(progress, scrollXProgress);
                        }
                    });
                // 设置是否支持左右滑
                flingCardListener.setIsNeedSwipe(isNeedSwipe);
                mActiveCard.setOnTouchListener(flingCardListener);

            }
        }
    }
    private boolean intercept;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        Log.e("hyc-touch","parent-----"+ev.toString());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (x - lastX == 0 && y - lastY == 0) {
                    return super.dispatchTouchEvent(ev);
                }
                if (intercept) {
                    return true;
                }
                if (forLeft) {
                    if (removeCount == 0) {
                        return true;
                    }
                    mAddViewY += (y - lastY);
                    mAddViewX += (x - lastX);
                    mAddView.setX(mAddViewX);
                    mAddView.setY(mAddViewY);
                    adjustChildrenOfUnderTopView(getScrollProgress(ev));
                    lastX = x;
                    lastY = y;
                    return true;
                } else if (forRight) {
                    lastX = x;
                    lastY = y;
                    return super.dispatchTouchEvent(ev);
                }
                if (Math.abs(x - lastX) > Math.abs(y - lastY) && x - lastX > 0) {
                    if (removeCount == 0) {
                        intercept=true;
                        return true;
                    }
                    forLeft = true;

                    int width = mAddView.getWidth();
                    mAddViewY = y - lastY;
                    mAddViewX = -width + x - lastX;
                    mAddView.setX(mAddViewX);
                    mAddView.setY(mAddViewY);
                    lastX = x;
                    lastY = y;
                    mAddView.setVisibility(VISIBLE);
                    return true;
                } else {
                    lastX = x;
                    lastY = y;
                    forRight = true;
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (intercept) {
                    intercept=false;
                    return true;
                }
                if (forLeft) {
                    backOrAdd();
                    return true;
                }
                forRight = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private float getScrollProgress(MotionEvent ev) {
        float dx = mAddView.getX() - x;
        float dy = mAddView.getX() - y;
        float dis = Math.abs(dx) + Math.abs(dy);
        return Math.min(dis, 400f) / 400f;
    }


    private void backOrAdd() {
        //if (removeCount==0) {
        //    if (mAddView.getX()>0||mAddView.getWidth()+mAddView.getX() > getWidth() / 4) {
        //        startScale=1;
        //        postDelayed(animRun,0);
        //        mAddView.animate().x(-mAddView.getWidth()).y(0).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        //    } else {
        //        forLeft=false;
        //        mAddView.animate().x(-mAddView.getWidth()).y(0).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        //    }
        //    return;
        //}
        if (mAddView.getX() > 0 || mAddView.getWidth() + mAddView.getX() > getWidth() / 4) {
            startScale = getScrollProgress(null);
            //postDelayed(animRun,0);
            mAddView.animate()
                .x(x)
                .y(y)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .start();
            startValueAnimator(startScale);

        } else {
            forLeft = false;
            mAddView.animate()
                .x(-mAddView.getWidth())
                .y(0)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        }

    }


    public FlingCardListener getTopCardListener() throws NullPointerException {
        if (flingCardListener == null) {
            throw new NullPointerException("flingCardListener is null");
        }
        return flingCardListener;
    }


    public void setMaxVisible(int MAX_VISIBLE) {
        this.MAX_VISIBLE = MAX_VISIBLE;
    }


    public void setMinStackInAdapter(int MIN_ADAPTER_STACK) {
        this.MIN_ADAPTER_STACK = MIN_ADAPTER_STACK;
    }


    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }

        mAdapter = adapter;

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }


    public void setFlingListener(onFlingListener onFlingListener) {
        this.mFlingListener = onFlingListener;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }


    public interface OnItemClickListener {
        void onItemClicked(MotionEvent event, View v, Object dataObject);
    }


    public interface onFlingListener {
        void removeFirstObjectInAdapter();
        void onLeftCardExit(Object dataObject);
        void onRightCardExit(Object dataObject);
        void onAdapterAboutToEmpty(int itemsInAdapter);
        void onScroll(float progress, float scrollXProgress);
        void onReAdd();
    }


    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }


        @Override
        public void onInvalidated() {
            requestLayout();
        }

    }

}
