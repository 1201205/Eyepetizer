package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.VideoListFragment;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/9/9.
 */
public class RankActivity extends BaseActivity {
    private static final long ANIMATION_DURATION = 350;
    @BindView(R.id.tv_week)
    CustomTextView tvWeek;
    @BindView(R.id.tv_month)
    CustomTextView tvMonth;
    @BindView(R.id.tv_all)
    CustomTextView tvAll;
    @BindView(R.id.ll_rank_bar)
    LinearLayout llRankBar;
    @BindView(R.id.vp_video)
    ViewPager vpVideo;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    @BindView(R.id.fl_all)
    FrameLayout flAll;
    @BindView(R.id.indicator)
    View indicator;
    @BindView(R.id.img_left)
    ImageView imgBack;
    @BindView(R.id.tv_head_title)
    CustomTextView tvTitle;
    private VideoListFragment mWeek;
    private VideoListFragment mMonth;
    private VideoListFragment mHistory;
    private FragmentAdapter mAdapter;
    private List<VideoListFragment> mFragments;
    private boolean isAnimating;
    private float mItemHeight;
    private float mTitleHeight;
    private float mRatio;
    private int lastY;
    private int mEndY;
    private int[] mIndicatorY;
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };
    private int mLastIndex;
    private int preIndex;
    private int mIndicatorScroll;
    private boolean isStarting;
    private int mLastType;


    @Override
    protected void handleIntent() {

    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initPresenterAndData() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        mIndicatorY = new int[3];
        mTitleHeight = AppUtil.dip2px(81);
        mItemHeight = AppUtil.dip2px(250);
        mRatio = AppUtil.dip2px(353) / mItemHeight;
        mEndY = (int) (AppUtil.getScreenHeight(this) - AppUtil.getStatusBarHeight(this) -
            mItemHeight - AppUtil.dip2px(60));
        indicator.getViewTreeObserver()
            .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    indicator.getViewTreeObserver().removeOnPreDrawListener(this);
                    int width = indicator.getWidth();
                    int textWidth = tvWeek.getWidth();
                    int[] location = new int[2];
                    tvWeek.getLocationInWindow(location);
                    mIndicatorY[0] = location[0] - (width - textWidth) / 2;
                    tvMonth.getLocationInWindow(location);
                    mIndicatorY[1] = location[0] - (width - textWidth) / 2;
                    indicator.setX(mIndicatorY[0]);
                    mIndicatorScroll = mIndicatorY[1] - mIndicatorY[0];
                    return true;
                }
            });
        initTitleBar();
        initViewPager();
    }


    private void initViewPager() {
        mFragments = new ArrayList<>();
        mWeek = VideoListFragment.instantiate(FromType.TYPE_WEEK, FromType.Tag.RANK_WEEK);
        mMonth = VideoListFragment.instantiate(FromType.TYPE_MONTH, FromType.Tag.RANK_MONTH);
        mHistory = VideoListFragment.instantiate(FromType.TYPE_HISTORY, FromType.Tag.RANK_HISTORY);
        mFragments.add(mWeek);
        mFragments.add(mMonth);
        mFragments.add(mHistory);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        vpVideo.setAdapter(mAdapter);
        vpVideo.setOffscreenPageLimit(2);
        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (preIndex > position) {
                    indicator.setX(
                        ((positionOffset - 1) * mIndicatorScroll) + mIndicatorScroll * preIndex +
                            mIndicatorY[0]);
                } else if (preIndex <= position) {
                    indicator.setX(positionOffset * mIndicatorScroll + mIndicatorScroll * preIndex +
                        mIndicatorY[0]);
                }
            }


            @Override
            public void onPageSelected(int position) {
                preIndex = position;
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initTitleBar() {
        imgBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.app_name);
        tvTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.LOBSTER));
    }


    @OnClick({ R.id.tv_all, R.id.tv_week, R.id.tv_month, R.id.img_left })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_week:
                vpVideo.setCurrentItem(0, true);
                break;
            case R.id.tv_month:
                vpVideo.setCurrentItem(1, true);
                break;
            case R.id.tv_all:
                vpVideo.setCurrentItem(2, true);
                break;
            case R.id.img_left:
                finish();
                break;
        }
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
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (isAnimating) {
            return;
        }
        if (event.fromType == FromType.TYPE_HISTORY || event.fromType == FromType.TYPE_MONTH ||
            event.fromType == FromType.TYPE_WEEK) {

            isAnimating = true;
            //EventBus.getDefault().post(new VideoSelectEvent(FromType.TYPE_DAILY,DailySelectionModel.getInstance().getMap().indexOfValue(event.position)));
            sdvAnim.setVisibility(View.VISIBLE);
            sdvAnim.setY(event.locationY - AppUtil.getStatusBarHeight(this));
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
                        Intent intent = VideoDetailActivity2.newIntent(event.fromType,
                            RankActivity.this, event.position,
                            event.parentIndex, event.fromType);
                        isStarting = true;
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                })
                .setInterpolator(mInterpolator)
                .start();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isStarting = false;
        isAnimating = false;
    }


    @Subscribe
    public void handleResumeAnim(final VideoDetailBackEvent event) {
        if (event.fromType == FromType.TYPE_HISTORY ||
            event.fromType == FromType.TYPE_MONTH ||
            event.fromType == FromType.TYPE_WEEK) {
            if (mLastType != event.fromType || mLastIndex != event.position) {
                FrescoHelper.loadUrl(sdvAnim, event.url);
            }
            mLastType = event.fromType;
            mLastIndex = event.position;

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

    }


    @Subscribe
    public void handleSelectEvent(VideoSelectEvent event) {
        if (mLastType != event.fromType || mLastIndex != event.position) {
            sdvAnim.setImageURI(VideoListModel.getInstance()
                .getVideo(event.fromType, event.position)
                .getData()
                .getCover()
                .getDetail());
            switch (event.fromType) {
                case FromType.TYPE_HISTORY:
                    mHistory.scrollTo(event.position);
                    break;
                case FromType.TYPE_WEEK:
                    mWeek.scrollTo(event.position);
                    break;
                case FromType.TYPE_MONTH:
                    mMonth.scrollTo(event.position);
                    break;
            }
            mLastType = event.fromType;
            mLastIndex = event.position;
        }
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
