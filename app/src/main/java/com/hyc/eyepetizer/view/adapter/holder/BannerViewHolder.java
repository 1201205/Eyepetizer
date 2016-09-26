package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 16/9/25.
 */
public class BannerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.vp_banner)
    public ViewPager vpBanner;
    @BindView(R.id.ll_indicator)
    public LinearLayout llIndicator;

    public BannerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
