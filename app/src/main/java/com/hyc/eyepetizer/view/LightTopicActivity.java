package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.LightTopicPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LightTopicActivity extends BaseActivity<LightTopicPresenter> implements VideoListContract.View{
    @BindView(R.id.rv_video)
    RecyclerView mRvVideo;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error)
    CustomTextView mTvError;
    @BindView(R.id.rl_error)
    RelativeLayout mRlError;
    private TestAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int mID;
    private static final long ANIMTION_DURATION = 350;
    @Override
    protected void handleIntent() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_special_topics;
    }


    @Override
    public void showList(List<ViewData> datas) {
        if (mAdapter == null) {
            if (mRvVideo.getVisibility()== View.GONE) {
                mRvVideo.setVisibility(View.VISIBLE);
                mRlError.setVisibility(View.GONE);
            }
            mAdapter = new TestAdapter.Builder(this, datas).build();
            mRvVideo.setAdapter(mAdapter);

        } else {
            mAdapter.addData(datas);
        }
    }
    private boolean isAnimating;
    private float mItemHeight;
    private float mTitleHeight;
    private float mRatio;
    private int lastY;
    private int mEndY;
    private boolean isStarting;
    @Override
    public void showError() {
        mRvVideo.setVisibility(View.GONE);
        mRlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMore() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initPresenter();
        mManager=new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvVideo.setLayoutManager(mManager);
    }

    private void initPresenter() {
        mPresenter=new LightTopicPresenter(this,mID);
        mPresenter.attachView();
        mPresenter.getVideoList();
    }
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    private static final long ANIMATION_DURATION = 350;
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };
    @Subscribe
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (isStarting||event.fromType != FromType.TYPE_MAIN) {
            return;
        }
        isStarting=true;
        sdvAnim.setVisibility(View.VISIBLE);
        sdvAnim.setY(event.locationY - getStatusBarHeight());
        FrescoHelper.loadUrl(sdvAnim, event.url);
        lastY = event.locationY;
        sdvAnim.animate()
                .scaleX(mRatio)
                .scaleY(mRatio)
                .y((mItemHeight * (mRatio - 1) / 2))
                .setDuration(ANIMTION_DURATION)
                .setListener(new MyAnimatorListener() {
                    @Override public void onAnimationEnd(Animator animator) {
                        isStarting=false;
                        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_MAIN,
                                LightTopicActivity.this, event.index,
                                event.parentIndex);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                })
                .setInterpolator(mInterpolator)
                .start();
    }


    @Subscribe
    public void handleResumeAnim(VideoDetailBackEvent event) {
        if (event.fromType != FromType.TYPE_MAIN) {
            return;
        }
        FrescoHelper.loadUrl(sdvAnim, event.url);
        if (event.hasScrolled) {
            if (event.theLast) {
                sdvAnim.animate()
                        .scaleX(1)
                        .setInterpolator(mInterpolator)
                        .scaleY(1)
                        .y(mEndY)
                        .setListener(mListener)
                        .setDuration(ANIMTION_DURATION)
                        .start();
            } else {
                sdvAnim.animate()
                        .scaleX(1)
                        .setInterpolator(mInterpolator)
                        .scaleY(1)
                        .y(mTitleHeight)
                        .setListener(mListener)
                        .setDuration(ANIMTION_DURATION)
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
                    .setDuration(ANIMTION_DURATION)
                    .setInterpolator(mInterpolator)
                    .start();
        }

    }
    public void handleSelectEvent(VideoSelectEvent event) {
        if (event.fromType != FromType.TYPE_MAIN || mLastIndex == event.position) {
            return;
        }
        mLastIndex=event.position;
        final int p=event.position+mStartPosition;
        mManager.scrollToPosition(p);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v=mRecyclerView.getChildAt(p-mManager.findFirstVisibleItemPosition());
                final int[] l=new int[2];
                v.getLocationInWindow(l);
                mRecyclerView.scrollBy(0,l[1]-getTitleHeight());
            }
        }, 5);
    }
}
