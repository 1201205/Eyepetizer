package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
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
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ray on 16/9/13.
 */

public class PagerListActivity extends AnimateActivity {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DATE_TYPE = "date_type";
    private static final String SHARE_TYPE = "share_type";
    private static final String HAS_MORE = "has_more";
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
    private int[] mIndicatorY;
    private int mLastIndex;
    private int preIndex;
    private int mIndicatorScroll;
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
    protected void initPresenterAndData() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initBar();
    }


    @Override protected void initEndY() {
        mEndY = (int) (AppUtil.getScreenHeight(this) - AppUtil.getStatusBarHeight(this) -
            mItemHeight - getResources().getDimensionPixelSize(R.dimen.no_more_height));
    }


    @Override
    protected void initView() {
        mIndicatorY = new int[2];
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


    //eyepetizer://tag

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


    @Override
    protected boolean canDeal(int type) {
        if (type <= FromType.TYPE_TAG_DATE ||
            type >= FromType.TYPE_CATEGORY_SHARE) {
            return true;
        }
        return false;
    }

    @Override
    protected void onStartAnimEnd(StartVideoDetailEvent event) {
        mLastType = event.fromType;
        mLastIndex = event.position;
        if (mLastType % 2 == 0) {
            mDate.setLastIndex(mLastIndex);
        } else {
            mShare.setLastIndex(mLastIndex);
        }
        Intent intent = VideoDetailActivity2.newIntent(event.fromType,
                PagerListActivity.this, event.position,
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


    @Override protected boolean hasIndicator() {
        return true;
    }


    @Override
    protected int getStartY(int y) {
        return y - AppUtil.getStatusBarHeight(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
