package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.VideoListFragment;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.ScrollTitleView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ray on 16/9/13.
 */

public class PgcActivity extends AnimateActivity {
    private static final String TITLE = "title";
    private static final String DES = "des";
    private static final String ID = "id";
    private static final String URL = "url";
    @BindView(R.id.vp_target)
    ViewPager vpVideo;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvHeadTitle;
    @BindView(R.id.tv_title)
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
    @BindView(R.id.main_content)
    LinearLayout mainContent;
    @BindView(R.id.cl_main)
    ScrollTitleView scrollTitleView;
    private String mTitle;
    private String mDes;
    private String mIcoUrl;
    private int mID;
    private VideoListFragment mDate;
    private VideoListFragment mShare;
    private FragmentAdapter mAdapter;
    private List<VideoListFragment> mFragments;
    private int[] mIndicatorY;
    private int mLastIndex;
    private int preIndex;
    private int mIndicatorScroll;
    private int mLastType;
    private int mLimitY;


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
    protected boolean canDeal(int type) {
        return type == FromType.TYPE_PGC_DATE ||
            type == FromType.TYPE_PGC_SHARE;
    }


    @Override
    protected void onStartAnimEnd(StartVideoDetailEvent event) {
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
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onStartResumeAnim(VideoDetailBackEvent event) {
        if (mLastType != event.fromType || mLastIndex != event.position) {
            FrescoHelper.loadUrl(sdvAnim, event.url);
        }
        mLastType = event.fromType;
        mLastIndex = event.position;
    }


    @Override
    protected boolean hasIndicator() {
        return true;
    }


    @Override
    protected int getStartY(int y) {
        return y - getStatusBarHeight();
    }


    @Override
    protected void initEndY() {
        mEndY = (int) (AppUtil.getScreenHeight(this) - getStatusBarHeight() -
            mItemHeight - mTitleHeight);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBar();
    }


    @Override
    protected void initView() {
        mIndicatorY = new int[3];

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
                    tvTitle.getLocationInWindow(location);
                    int titleY = location[1];
                    tvHeadTitle.getLocationInWindow(location);
                    int headY = location[1];
                    mLimitY = titleY - headY;
                    tvHeadTitle.setTranslationY(mLimitY);
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
        scrollTitleView.setContainer(new ScrollTitleView.ScrollableContainer() {
            @Override
            public RecyclerView getScrollableView() {
                return mFragments.get(preIndex).getScrollView();
            }
        });
        scrollTitleView.setListener(new ScrollTitleView.ScrollListener() {
            @Override public void scroll(int y) {
                if (y >= mLimitY) {
                    tvHeadTitle.setTranslationY(0);
                } else if (y > 0) {
                    tvHeadTitle.setTranslationY(mLimitY - y);
                } else {
                    tvHeadTitle.setTranslationY(mLimitY);

                }
            }
        });
    }


    private void initBar() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvHeadTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        tvHeadTitle.setText(mTitle);
        tvTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        tvTitle.setText(mTitle);
    }


    @Subscribe
    public void handleSelectEvent(final VideoSelectEvent event) {
        if (mLastType != event.fromType || mLastIndex != event.position) {
            FrescoHelper.loadUrl(sdvAnim, event.url);
            scrollTitleView.closeHead();
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

}
