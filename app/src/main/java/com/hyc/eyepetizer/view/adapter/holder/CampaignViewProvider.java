package com.hyc.eyepetizer.view.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.RouterWrapper;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 广告----
 * Created by Administrator on 2016/8/31.
 */
public class CampaignViewProvider extends ItemViewProvider<CampaignViewProvider.CampaignViewHolder>{
    @Override
    protected CampaignViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new CampaignViewHolder(inflater.inflate(R.layout.item_campaign, parent, false));
    }

    @Override
    protected void onBindViewHolder(CampaignViewHolder holder, final ViewData data, int position, AdapterParameterWrapper wrapper) {
        FrescoHelper.loadUrl(holder.campaign, data.getData().getImage());
        holder.campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=v.getContext();
                RouterWrapper wrapper =  DataHelper.getIntentByUri(context,data.getData().getActionUrl());
                if (wrapper.getIntent()!=null) {
                    context.startActivity(wrapper.getIntent());
                } else if (wrapper.getEvent()!=null) {
                    EventBus.getDefault().post(wrapper.getEvent());
                }

            }
        });
    }

    static class CampaignViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.sdv_campaign)
    public SimpleDraweeView campaign;
    public CampaignViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
}
