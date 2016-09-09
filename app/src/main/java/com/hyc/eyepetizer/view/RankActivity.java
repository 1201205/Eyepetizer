package com.hyc.eyepetizer.view;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/9.
 */
public class RankActivity extends BaseActivity {
    @BindView(R.id.tv_week)
    CustomTextView tvWeek;
    @BindView(R.id.tv_month)
    CustomTextView tvMonth;
    @BindView(R.id.tv_all)
    CustomTextView tvAll;
    @BindView(R.id.ll_rank_bar)
    LinearLayout llRankBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    @BindView(R.id.fl_all)
    FrameLayout flAll;

    @Override
    protected void handleIntent() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_rank;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
