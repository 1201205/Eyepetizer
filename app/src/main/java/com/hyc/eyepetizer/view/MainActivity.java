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
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.TestFragment;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private static final long ANIMTION_DURATION = 350;
    @BindView(R.id.test1)
    ViewPager mPager;
    @BindView(R.id.tv_title)
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
    //recyclerView中video高度
    private float mItemHeight;
    private float mTitleHeight;
    private float mRatio;
    private int lastY;
    private int mEndY;
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mStatusBarHeight;
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mRlAll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRlAll.getViewTreeObserver().removeOnPreDrawListener(this);
                mEndY= (int) (mRlAll.getHeight()-AppUtil.dip2px(350));
                return true;
            }
        });
        mTitleHeight = AppUtil.dip2px(45);
        mItemHeight = AppUtil.dip2px(250);
        mRatio = AppUtil.dip2px(353) / mItemHeight;
        Log.e("hyc_i",AppUtil.getScreenHeight(this)+"--");
        //getWindow().setSharedElementEnterTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
        //    ScalingUtils.ScaleType.CENTER_CROP));
        //getWindow().setSharedElementReturnTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
        //    ScalingUtils.ScaleType.CENTER_CROP));
        mTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.LOBSTER));
        mTitle.setText(R.string.app_name);
        List<Fragment> fragments = new ArrayList<>();
        mTestFragment = new TestFragment();
        fragments.add(mTestFragment);
        //fragments.add(new TestFragment());
        //fragments.add(new TestFragment());
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
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (event.fromType != FromType.TYPE_MAIN) {
            return;
        }
        if (mTestFragment.isLoading()) {
            return;
        }
        sdvAnim.setVisibility(View.VISIBLE);
        sdvAnim.setY(event.locationY - getStatusBarHeight());
        FrescoHelper.loadUrl(sdvAnim, event.url);
        lastY = event.locationY;
        mTestFragment.setStartPosition(event.position - event.index);
        sdvAnim.animate()
            .scaleX(mRatio)
            .scaleY(mRatio)
            .y((mItemHeight * (mRatio - 1) / 2))
            .setDuration(ANIMTION_DURATION)
            .setListener(new MyAnimatorListener() {
                @Override public void onAnimationEnd(Animator animator) {
                    Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_MAIN,
                        MainActivity.this, event.index,
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


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            mStatusBarHeight = AppUtil.getStatusBarHeight(this);
        }
        return mStatusBarHeight;
    }
}
