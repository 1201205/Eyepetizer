package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
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
 * Created by ray on 16/9/13.
 */

public class PgcActivity extends BaseActivity {
    private static final String TITLE = "title";
    private static final String DES = "des";
    private static final String ID = "id";
    private static final String URL = "url";
    private static final long ANIMATION_DURATION = 350;
    @BindView(R.id.vp_target)
    ViewPager vpVideo;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvTitle;
    @BindView(R.id.sdv_icon)
    SimpleDraweeView sdvIcon;
    @BindView(R.id.tv_des)
    CustomTextView tvDes;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.tv_share)
    CustomTextView tvShare;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.indicator)
    View indicator;
    @BindView(R.id.rl_indicator)
    RelativeLayout rlIndicator;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.cl_main)
    CoordinatorLayout clMain;
    @BindView(R.id.main_content)
    LinearLayout mainContent;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    private String mTitle;
    private String mDes;
    private String mIcoUrl;
    private int mID;
    private VideoListFragment mDate;
    private VideoListFragment mShare;
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
    private int i = 1;


    public static void start(Context context, String title, String des, String url, int id) {
        Intent intent = new Intent(context, PgcActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(DES, des);
        intent.putExtra(URL, url);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }


    @Override
    protected void handleIntent() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(TITLE);
        mDes = intent.getStringExtra(DES);
        mIcoUrl = intent.getStringExtra(URL);
        mID = intent.getIntExtra(ID, 0);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_pgc;
    }

    @Override
    protected void initPresenterAndData() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initBar();
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
                    //                    appbar.setExpanded(false);
                    int width = indicator.getWidth();
                    int textWidth = tvTime.getWidth();
                    int[] location = new int[2];
                    tvTime.getLocationInWindow(location);
                    mIndicatorY[0] = location[0] - (width - textWidth) / 2;
                    tvShare.getLocationInWindow(location);
                    mIndicatorY[1] = location[0] - (width - textWidth) / 2;
                    indicator.setX(mIndicatorY[0]);
                    mIndicatorScroll = mIndicatorY[1] - mIndicatorY[0];
                    return true;
                }
            });
        FrescoHelper.loadUrl(sdvIcon, mIcoUrl);
        tvDes.setText(mDes);
        mFragments = new ArrayList<>();
        mDate = VideoListFragment.instantiate(FromType.TYPE_PGC_DATE, FromType.Tag.DATE, mID, true);
        mShare = VideoListFragment.instantiate(FromType.TYPE_PGC_SHARE, FromType.Tag.SHARE_COUNT,
            mID, true);
        mFragments.add(mDate);
        mFragments.add(mShare);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        vpVideo.setAdapter(mAdapter);
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


    private void initBar() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                int[] l = new int[2];
                //                rlIndicator.getLocationInWindow(l);
                finish();
                //                appbar.setExpanded(false);
                //                mDate.scrollTo(i, (int) (l[1] - AppUtil.dip2px(45) - AppUtil.getStatusBarHeight(PgcActivity.this)));
                //                i++;
            }
        });
        tvTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        tvTitle.setText(mTitle);
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
        Log.e("hyc-po", event.position + "--VideoDetailBackEvent--");
        if (event.fromType == FromType.TYPE_PGC_DATE ||
            event.fromType == FromType.TYPE_PGC_SHARE) {
            if (mLastType != event.fromType || mLastIndex != event.position) {
                FrescoHelper.loadUrl(sdvAnim, event.url);
            }
            //Log.e("hyc-po", event.position + "--VideoDetailBackEvent--");
            mLastType = event.fromType;
            mLastIndex = event.position;

            if (event.hasScrolled) {
                appbar.setExpanded(false);
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
    public void handleSelectEvent(final VideoSelectEvent event) {
        if (mLastType != event.fromType || mLastIndex != event.position) {
            Log.e("hyc-po", event.position + "--VideoSelectEvent--");
            //            appbar.postDelayed(new Runnable() {
            //                @Override
            //                public void run() {
            //                    appbar.setExpanded(false);
            //                }
            //            },0);
            appbar.setExpanded(false);
            sdvAnim.setImageURI(VideoListModel.getInstance()
                .getVideo(event.fromType, event.position)
                .getData()
                .getCover()
                .getDetail());
            int[] l = new int[2];
            rlIndicator.getLocationInWindow(l);
            switch (event.fromType) {
                case FromType.TYPE_PGC_DATE:
                    mDate.scrollTo(event.position,
                        (int) (l[1] - AppUtil.dip2px(45) - AppUtil.getStatusBarHeight(this)));
                    break;
                case FromType.TYPE_PGC_SHARE:
                    mShare.scrollTo(event.position,
                        (int) (l[1] - AppUtil.dip2px(45) - AppUtil.getStatusBarHeight(this)));
                    break;
            }
            mLastType = event.fromType;
            mLastIndex = event.position;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isStarting = false;
        isAnimating = false;
    }


    @OnClick({ R.id.img_left, R.id.tv_time, R.id.tv_share })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                finish();
                break;
            case R.id.tv_time:
                vpVideo.setCurrentItem(0, true);
                break;
            case R.id.tv_share:
                vpVideo.setCurrentItem(1, true);
                break;
        }
    }


    @Subscribe
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (isAnimating) {
            return;
        }
        if (event.fromType == FromType.TYPE_PGC_DATE ||
            event.fromType == FromType.TYPE_PGC_SHARE) {

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
                        mLastType = event.fromType;
                        mLastIndex = event.position;
                        if (mLastType == FromType.TYPE_PGC_DATE) {
                            mDate.setLastIndex(mLastIndex);
                        } else {
                            mShare.setLastIndex(mLastIndex);
                        }
                        Intent intent = VideoDetailActivity2.newIntent(event.fromType,
                            PgcActivity.this, event.position,
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
