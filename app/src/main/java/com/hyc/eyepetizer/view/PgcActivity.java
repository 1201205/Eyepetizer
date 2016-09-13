package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.view.adapter.FragmentAdapter;
import com.hyc.eyepetizer.view.fragment.VideoListFragment;
import com.hyc.eyepetizer.widget.MyAnimatorListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ray on 16/9/13.
 */
public class PgcActivity extends BaseActivity {

    @Override protected void handleIntent() {

    }


    @Override protected int getLayoutID() {
        return R.layout.activity_new;
    }
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animator) {
        }
    };
    private VideoListFragment mWeek;
    private VideoListFragment mMonth;
    private VideoListFragment mHistory;
    private FragmentAdapter mAdapter;
    private List<VideoListFragment> mFragments;
    ViewPager vpVideo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpVideo= (ViewPager) findViewById(R.id.vp_target);
        mFragments = new ArrayList<>();
        mWeek = VideoListFragment.instantiate(FromType.TYPE_WEEK, FromType.Tag.RANK_WEEK);
        mMonth = VideoListFragment.instantiate(FromType.TYPE_MONTH, FromType.Tag.RANK_MONTH);
        mHistory = VideoListFragment.instantiate(FromType.TYPE_HISTORY, FromType.Tag.RANK_HISTORY);
        mFragments.add(mWeek);
//        mFragments.add(mMonth);
//        mFragments.add(mHistory);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        vpVideo.setAdapter(mAdapter);
        vpVideo.setOffscreenPageLimit(2);
//        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (preIndex > position) {
//                    indicator.setX(((positionOffset - 1) * mIndicatorScroll) + mIndicatorScroll * preIndex+mIndicatorY[0]);
//                } else if (preIndex <= position) {
//                    indicator.setX(positionOffset * mIndicatorScroll + mIndicatorScroll * preIndex+mIndicatorY[0]);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                preIndex=position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }
}
