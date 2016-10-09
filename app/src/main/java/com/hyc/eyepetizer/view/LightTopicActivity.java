package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.LightTopicPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LightTopicActivity extends BaseActivity<LightTopicPresenter> implements VideoListContract.View{
    private static final long ANIMATION_DURATION = 350;
    @BindView(R.id.rv_video)
    RecyclerView mRvVideo;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error)
    CustomTextView mTvError;
    @BindView(R.id.rl_error)
    RelativeLayout mRlError;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvHeadTitle;
    private TestAdapter mAdapter;
    private LinearLayoutManager mManager;
    private boolean isAnimating;
    private float mItemHeight;
    private float mTitleHeight;
    private int lastY;
    private int mEndY;
    private boolean isStarting;
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };
    private int mLastIndex;
    private int mID;
    private String mTitle;
    private float mRatio;


    public static Intent getIntent(Context context, String title, int id) {
        Intent intent = new Intent(context, LightTopicActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        return intent;
    }


    @Override
    protected void handleIntent() {
        Intent intent = getIntent();
        mID = intent.getIntExtra("id", -1);
        mTitle = intent.getStringExtra("title");
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_light_topics;
    }


    @Override
    public void showList(List<ViewData> datas) {
        if (mAdapter == null) {
            if (mRvVideo.getVisibility()== View.GONE) {
                mRvVideo.setVisibility(View.VISIBLE);
                mRlError.setVisibility(View.GONE);
            }
            mAdapter = new TestAdapter.Builder(this, datas).type(FromType.TYPE_LIGHT_TOPIC).build();
            mRvVideo.setAdapter(mAdapter);

        } else {
            mAdapter.addData(datas);
        }
    }


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
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initPresenter();
        initView();
        mManager=new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvVideo.setLayoutManager(mManager);
    }


    private void initView() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvHeadTitle.setText(mTitle);
        tvHeadTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        mTitleHeight = AppUtil.dip2px(45);
        mItemHeight = AppUtil.dip2px(250);
        mRatio = AppUtil.dip2px(353) / mItemHeight;
        mEndY = (int) (AppUtil.getScreenHeight(this) - AppUtil.getStatusBarHeight(this) -
            mItemHeight - AppUtil.dip2px(60));
    }


    private void initPresenter() {
        mPresenter=new LightTopicPresenter(this,mID);
        mPresenter.attachView();
        mPresenter.getVideoList();
    }


    @Subscribe
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (isAnimating || event.fromType != FromType.TYPE_LIGHT_TOPIC) {
            return;
        }
        isAnimating = true;

        sdvAnim.setVisibility(View.VISIBLE);
        sdvAnim.setY(event.locationY - AppUtil.getStatusBarHeight(LightTopicActivity.this));
        FrescoHelper.loadUrl(sdvAnim, event.url);
        lastY = event.locationY;
        sdvAnim.animate()
                .scaleX(mRatio)
                .scaleY(mRatio)
                .y((mItemHeight * (mRatio - 1) / 2))
            .setDuration(ANIMATION_DURATION)
                .setListener(new MyAnimatorListener() {
                    @Override public void onAnimationEnd(Animator animator) {
                        isStarting = true;
                        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_LIGHT_TOPIC,
                                LightTopicActivity.this, event.index,
                            event.parentIndex, mID);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                })
                .setInterpolator(mInterpolator)
                .start();
    }


    @Override protected void onResume() {
        super.onResume();
        isStarting = false;
        isAnimating = false;
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void handleResumeAnim(VideoDetailBackEvent event) {
        if (event.fromType != FromType.TYPE_LIGHT_TOPIC) {
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
    public void handleSelectEvent(VideoSelectEvent event) {
        if (event.fromType != FromType.TYPE_MAIN || mLastIndex == event.position) {
            return;
        }
        mLastIndex=event.position;
        mManager.scrollToPosition(mLastIndex);
        mRvVideo.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v = mRvVideo.getChildAt(mLastIndex - mManager.findFirstVisibleItemPosition());
                final int[] l=new int[2];
                v.getLocationInWindow(l);
                mRvVideo.scrollBy(0, l[1] - AppUtil.getStatusBarHeight(LightTopicActivity.this));
            }
        }, 5);
    }
}
