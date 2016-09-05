package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.StartVideoEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.TestFragment;
import com.hyc.eyepetizer.widget.CustomTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
    private TestFragment mTestFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        //TransitionSet transitionSet = new TransitionSet();
        //transitionSet.addTransition(new ChangeBounds());
        //transitionSet.addTransition(new DraweeTransition(ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.FIT_CENTER));
        //getWindow().setSharedElementEnterTransition(transitionSet);
        //
        //getWindow().setSharedElementReturnTransition(transitionSet);
        //getWindow().setSharedElementEnterTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
        //    ScalingUtils.ScaleType.CENTER_CROP));
        //getWindow().setSharedElementReturnTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
        //    ScalingUtils.ScaleType.CENTER_CROP));
        mTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.LOBSTER));
        mTitle.setText(R.string.app_name);
        List<Fragment> fragments=new ArrayList<>();
        mTestFragment=new TestFragment();
        fragments.add(mTestFragment);
        //fragments.add(new TestFragment());
        //fragments.add(new TestFragment());
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragments);
        mPager.setAdapter(adapter);
        for (int i=0;i<mTab.getChildCount();i++) {
            RadioButton button=(RadioButton)mTab.getChildAt(i);
            button.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.NORMAL));
            final int finalI = i;
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        mPager.setCurrentItem(finalI,true);
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
    private int lastY;
    private int startPosition;
    private int mRecyclerBottom=-1;
    @Subscribe
    public void handleStartActivity(final StartVideoEvent event){
        startPosition=event.position-event.index;
        sdvAnim.setVisibility(View.VISIBLE);
        sdvAnim.setY(event.location[3]);
        FrescoHelper.loadUrl(sdvAnim,event.url);
        lastY=event.location[3];
        mTestFragment.setStartPosition(event.position-event.index);
        sdvAnim.animate().scaleX(1058/750f).scaleY(1058/750f).y((float) (750f*(1058/750f-1)/2)).setDuration(350).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent = VideoDetailActivity2.newIntent(MainActivity.this, event.index,
                       event.parentIndex);
                startActivity(intent);
                overridePendingTransition(0,0);
                int[] l=new int[2];
                sdvAnim.getLocationInWindow(l);
                Log.e("test-1",sdvAnim.getWidth()+"-----"+sdvAnim.getHeight()+"-----"+l[0]+"----"+l[1]);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }


    @Subscribe
    public void handleResumeAnim(VideoSelectEvent event){
        FrescoHelper.loadUrl(sdvAnim,event.url);
        if (event.hasScrolled) {
            int[] l = new int[2];
            sdvAnim.getLocationInWindow(l);
            sdvAnim.animate().scaleX(1).scaleY(1).y(AppUtil.dip2px(45) - l[1]).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    sdvAnim.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).setDuration(350).start();
        } else {
            int[] l=new int[2];
            sdvAnim.getLocationInWindow(l);
            sdvAnim.animate().scaleX(1).scaleY(1).y(lastY-l[1]).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    sdvAnim.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).setDuration(350).start();
        }
//        if (event.position+startPosition > mTestFragment.getLastVisiblePosition()) {
//            mTestFragment.scrollToPosition(event.position+startPosition);
//            if (mRecyclerBottom==-1) {
//                mRecyclerBottom=mTestFragment.getBottom();
//            }
//            int[] l=new int[2];
//            sdvAnim.getLocationInWindow(l);
//            sdvAnim.animate().scaleY(1).scaleX(1).y(mRecyclerBottom-750-l[1]).setDuration(350).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    sdvAnim.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            }).start();
//        }else
//        if (startPosition+event.position<mTestFragment.getFirstVisiblePosition()) {
//            mTestFragment.scrollToPosition(event.position+startPosition);
//            sdvAnim.animate().scaleY(1).scaleX(1).setDuration(350).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    sdvAnim.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            }).start();
//        } else {
//            int[] l=new int[2];
//            sdvAnim.getLocationInWindow(l);
//            sdvAnim.animate().scaleX(1).scaleY(1).y(lastY-l[1]).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    sdvAnim.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            }).setDuration(350).start();
//        }

    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public int getTitleHeight(){
        return rlTitle.getHeight();
    }
}
