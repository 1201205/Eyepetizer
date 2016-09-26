package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PagerListActivity extends BaseActivity {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DATE_TYPE = "date_type";
    private static final String SHARE_TYPE = "share_type";
    private static final String HAS_MORE = "has_more";
    private static final long ANIMATION_DURATION = 350;
    @BindView(R.id.vp_video)
    ViewPager vpVideo;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvTitle;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.tv_share)
    CustomTextView tvShare;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.indicator)
    View indicator;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
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
    private String mTitle;
    private int mShareType;
    private int mDateType;
    private boolean mHasMore;


    public static void start(Context context, String title, int id, int dateType, int shareType, boolean hasMore) {
        context.startActivity(getIntent(context, title, id, dateType, shareType, hasMore));
    }


    public static Intent getIntent(Context context, String title, int id, int dateType, int shareType, boolean hasMore) {
        Intent intent = new Intent(context, PagerListActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(SHARE_TYPE, shareType);
        intent.putExtra(DATE_TYPE, dateType);
        intent.putExtra(HAS_MORE, hasMore);
        return intent;
    }

    @Override
    protected void handleIntent() {
        Intent intent = getIntent();
        mID = intent.getIntExtra(ID, 0);
        mTitle = intent.getStringExtra(TITLE);
        mDateType = intent.getIntExtra(DATE_TYPE, 0);
        mShareType = intent.getIntExtra(SHARE_TYPE, 0);
        mHasMore = intent.getBooleanExtra(HAS_MORE, false);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_pager_list;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initBar();
        initView();
    }


    private void initView() {
        mIndicatorY = new int[2];
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
        mFragments = new ArrayList<>();
        mDate = VideoListFragment.instantiate(mDateType, FromType.Tag.DATE, mID, mHasMore);
        mShare = VideoListFragment.instantiate(mShareType, FromType.Tag.SHARE_COUNT,
            mID, mHasMore);
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
    //eyepetizer://tag


    @Subscribe
    public void handleResumeAnim(final VideoDetailBackEvent event) {
        Log.e("hyc-po", event.position + "--VideoDetailBackEvent--");
        if (!canDeal(event.fromType)) {
            return;
        }
        if (mLastType != event.fromType || mLastIndex != event.position) {
            FrescoHelper.loadUrl(sdvAnim, event.url);
        }
        Log.e("hyc-po", event.position + "--VideoDetailBackEvent--");
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


    @Subscribe
    public void handleSelectEvent(final VideoSelectEvent event) {
        if (!canDeal(event.fromType) ||
            (mLastType == event.fromType && mLastIndex == event.position)) {
            return;
        }
        Log.e("hyc-po", event.position + "--VideoSelectEvent--");
        //            appbar.postDelayed(new Runnable() {
        //                @Override
        //                public void run() {
        //                    appbar.setExpanded(false);
        //                }
        //            },0);
        sdvAnim.setImageURI(VideoListModel.getInstance()
            .getVideo(event.fromType, event.position)
            .getData()
            .getCover()
            .getDetail());
        if (event.fromType % 2 == 0) {
            mDate.scrollTo(event.position);
        } else {
            mShare.scrollTo(event.position);
        }
        mLastType = event.fromType;
        mLastIndex = event.position;

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
        if (!canDeal(event.fromType)) {
            return;
        }
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
                        PagerListActivity.this, event.position,
                        event.parentIndex, event.fromType);
                    isStarting = true;
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            })
            .setInterpolator(mInterpolator)
            .start();
    }


    private boolean canDeal(int type) {
        if (type <= FromType.TYPE_TAG_DATE ||
            type >= FromType.TYPE_CATEGORY_SHARE) {
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
