package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.widget.MyAnimatorListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by hyc on 2016/10/11.
 */
public abstract class AnimateActivity<E extends BasePresenter> extends BaseActivity<E>{
    protected static final long ANIMATION_DURATION = 350;
    @BindView(R.id.sdv_anim)
    protected SimpleDraweeView sdvAnim;
    protected boolean isAnimating;
    protected float mItemHeight;
    protected float mTitleHeight;
    protected float mRatio;
    protected int lastY;
    protected int mEndY;
    protected boolean isStarting;
    protected AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    protected MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };
    protected abstract boolean canDeal(int type);
    protected abstract void onStartAnimEnd(StartVideoDetailEvent event);
    protected abstract void onStartResumeAnim(VideoDetailBackEvent event);
    protected abstract boolean hasIndicator();
    protected abstract int getStartY(int y);
    protected abstract void initEndY();

    protected boolean isLoading(){
        return false;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initParameter();
    }

    protected void initParameter(){
        Resources resources=getResources();
        mTitleHeight=resources.getDimension(R.dimen.title_height);
        if (hasIndicator()) {
            mTitleHeight+=resources.getDimension(R.dimen.indicator_height);
        }
        mItemHeight = resources.getDimension(R.dimen.home_img_height);
        mRatio = resources.getDimension(R.dimen.detail_img_height) / mItemHeight;
        initEndY();
    }


    @Override
    protected void onResume() {
        super.onResume();
        isStarting = false;
        isAnimating = false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAnimating || isStarting) {
            Log.e("dispatchTouchEvent", "dispatchTouchEvent");
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Subscribe
    public void handleResumeAnim(final VideoDetailBackEvent event) {
        if (!canDeal(event.fromType)) {
            return;
        }
        onStartResumeAnim(event);
        if (event.hasScrolled) {
            if (event.theLast) {
                sdvAnim.animate()
                        .scaleX(1)
                        .setInterpolator(mInterpolator)
                        .scaleY(1)
                        .y(mEndY)
                        .setListener(mListener)
                        .setDuration(ANIMATION_DURATION)
                        .start();
            } else {
                sdvAnim.animate()
                        .scaleX(1)
                        .setInterpolator(mInterpolator)
                        .scaleY(1)
                        .y(mTitleHeight)
                        .setListener(mListener)
                        .setDuration(ANIMATION_DURATION)
                        .start();
            }
        } else {
            int[] l = new int[2];
            sdvAnim.getLocationInWindow(l);
            sdvAnim.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .y(lastY - l[1])
                    .setListener(mListener)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(mInterpolator)
                    .start();
        }

    }
    @Subscribe
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (isAnimating||isLoading()) {
            return;
        }
        if (!canDeal(event.fromType)) {
            return;
        }
        isAnimating = true;
        //EventBus.getDefault().post(new VideoSelectEvent(FromType.TYPE_DAILY,DailySelectionModel.getInstance().getMap().indexOfValue(event.position)));
        sdvAnim.setVisibility(View.VISIBLE);
        sdvAnim.setY(getStartY(event.locationY));
        FrescoHelper.loadUrl(sdvAnim, event.url);
        lastY = event.locationY;
        sdvAnim.animate()
                .scaleX(mRatio)
                .scaleY(mRatio)
                .y((mItemHeight * (mRatio - 1) / 2))
                .setDuration(ANIMATION_DURATION)
                .setListener(new MyAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        onStartAnimEnd(event);
                        isStarting = true;
                    }
                })
                .setInterpolator(mInterpolator)
                .start();
    }
    protected int mStatusBarHeight;
    protected int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            mStatusBarHeight = AppUtil.getStatusBarHeight(this);
        }
        return mStatusBarHeight;
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
