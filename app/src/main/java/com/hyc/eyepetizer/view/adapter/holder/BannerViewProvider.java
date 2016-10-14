package com.hyc.eyepetizer.view.adapter.holder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;
import com.hyc.eyepetizer.view.adapter.LoopViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 16/9/25.
 */
public class BannerViewProvider extends ItemViewProvider<BannerViewProvider.BannerViewHolder>{
    @Override
    protected BannerViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new BannerViewHolder(inflater.inflate(R.layout.item_pager,parent,false));
    }

    @Override
    protected void onBindViewHolder(BannerViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        if (holder.vpBanner.getAdapter()==null) {
            final Context context=holder.itemView.getContext();
            LoopViewPagerAdapter adapter=new LoopViewPagerAdapter(holder.vpBanner,holder.llIndicator);
            adapter.setList(data.getData().getItemList());
            holder.vpBanner.setAdapter(adapter);
            holder.vpBanner.addOnPageChangeListener(adapter);
            adapter.notifyDataSetChanged();
            adapter.setItemClickListener(new LoopViewPagerAdapter.ItemClickListener() {
                @Override public void itemClicked(ViewData data) {
                    context.startActivity(
                            DataHelper.getIntentByUri(context, data.getData().getActionUrl())
                                    .getIntent());
                }
            });
        }
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.vp_banner)
    public ViewPager vpBanner;
    @BindView(R.id.ll_indicator)
    public LinearLayout llIndicator;

    public BannerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}}
