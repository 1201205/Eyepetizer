package com.hyc.eyepetizer.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.HomePageEvent;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.DiscoveryFragment;
import com.hyc.eyepetizer.view.fragment.PgcFragment;
import com.hyc.eyepetizer.view.fragment.TestFragment;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AnimateActivity {
    @BindView(R.id.test1)
    ViewPager mPager;
    @BindView(R.id.tv_head_title)
    CustomTextView mTitle;
    @BindView(R.id.rg_tab)
    RadioGroup mTab;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.fl_all)
    FrameLayout mRlAll;
    private TestFragment mTestFragment;
    private DiscoveryFragment mDiscoveryFragment;
    private PgcFragment mPgcFragment;
    //recyclerView中video高度
    private int mLastIndex;
    private int mStartPosition;


    @Override
    protected boolean canDeal(int type) {
        return type == FromType.TYPE_MAIN;
    }


    @Override
    protected boolean isLoading() {
        return mTestFragment.isLoading();
    }


    @Override
    protected void onStartAnimEnd(StartVideoDetailEvent event) {
        mStartPosition = event.position - event.index;
        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_MAIN,
            MainActivity.this, event.index,
            event.parentIndex);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onStartResumeAnim(VideoDetailBackEvent event) {
        if (mLastIndex != event.position) {
            FrescoHelper.loadUrl(sdvAnim, event.url);
        }
    }


    @Subscribe
    public void handleSelectEvent(VideoSelectEvent event) {
        if (event.fromType != FromType.TYPE_MAIN || mLastIndex == event.position) {
            return;
        }
        FrescoHelper.loadUrl(sdvAnim, event.url);
        mLastIndex = event.position;
        mTestFragment.scrollToPosition(event.position + mStartPosition);

    }
    @Override
    protected boolean hasIndicator() {
        return false;
    }


    @Override
    protected int getStartY(int y) {
        return y - getStatusBarHeight();
    }


    @Override
    protected void initEndY() {
        mRlAll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRlAll.getViewTreeObserver().removeOnPreDrawListener(this);
                mEndY = (int) (mRlAll.getHeight() - AppUtil.dip2px(370));
                return true;
            }
        });
    }


    @Override
    protected void handleIntent() {

    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_main;
    }


    @Override
    protected void initPresenterAndData() {

    }


    @Override
    protected void initView() {

        mTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.LOBSTER));
        mTitle.setText(R.string.app_name);
        List<Fragment> fragments = new ArrayList<>();
        mTestFragment = new TestFragment();
        fragments.add(mTestFragment);
        mDiscoveryFragment = new DiscoveryFragment();
        fragments.add(mDiscoveryFragment);
        mPgcFragment = new PgcFragment();
        fragments.add(mPgcFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(adapter);
        for (int i = 0; i < mTab.getChildCount(); i++) {
            RadioButton button = (RadioButton) mTab.getChildAt(i);
            button.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.NORMAL));
            final int finalI = i;
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        mPager.setCurrentItem(finalI, true);
                    }
                }
            });
        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                ((RadioButton) mTab.getChildAt(position)).setChecked(true);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Subscribe
    public void goToPage(HomePageEvent event) {
        if ("pgcs".equals(event.targrt)) {
            mPager.setCurrentItem(2, true);
        } else if ("discovery".equals(event.targrt)) {
            mPager.setCurrentItem(1, true);
        }
    }

}
