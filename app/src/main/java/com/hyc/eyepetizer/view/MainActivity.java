package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.HomePageEvent;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.DiscoveryFragment;
import com.hyc.eyepetizer.view.fragment.PgcFragment;
import com.hyc.eyepetizer.view.fragment.TestFragment;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AnimateActivity {
    private static final long ANIMTION_DURATION = 350;
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
    private float mItemHeight;
    private float mTitleHeight;
    private float mRatio;
    private int lastY;
    private int mEndY;
    private boolean isStarting;
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mStatusBarHeight;
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };


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
        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_MAIN,
                MainActivity.this, event.index,
                event.parentIndex);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStartResumeAnim(VideoDetailBackEvent event) {
        FrescoHelper.loadUrl(sdvAnim, event.url);
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
        mRlAll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRlAll.getViewTreeObserver().removeOnPreDrawListener(this);
                mEndY= (int) (mRlAll.getHeight()-AppUtil.dip2px(350));
                return true;
            }
        });
        mTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.LOBSTER));
        mTitle.setText(R.string.app_name);
        List<Fragment> fragments = new ArrayList<>();
        mTestFragment = new TestFragment();
        fragments.add(mTestFragment);
        mDiscoveryFragment=new DiscoveryFragment();
        fragments.add(mDiscoveryFragment);
        mPgcFragment=new PgcFragment();
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
