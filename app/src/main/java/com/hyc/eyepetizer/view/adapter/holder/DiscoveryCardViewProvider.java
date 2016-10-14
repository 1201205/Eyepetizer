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
 * Created by ray on 16/9/25.
 */
public class DiscoveryCardViewProvider extends ItemViewProvider<DiscoveryCardViewProvider.DiscoveryCardViewHolder> {
    @Override
    protected DiscoveryCardViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new DiscoveryCardViewHolder(inflater.inflate(R.layout.item_rectangle, parent, false));
    }

    @Override
    protected void onBindViewHolder(DiscoveryCardViewHolder holder, final ViewData data, int position, AdapterParameterWrapper wrapper) {
        FrescoHelper.loadUrl(holder.sdvImage, data.getData().getImage());
        holder.sdvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                RouterWrapper wrapper;
                if (data.getData().getHeader() != null) {
                    wrapper = DataHelper.getIntentByUri(context,
                            data.getData().getHeader().getActionUrl());
                } else {
                    wrapper = DataHelper.getIntentByUri(context,
                            data.getData().getActionUrl());
                }
                if (wrapper.getIntent() != null) {
                    context.startActivity(wrapper.getIntent());
                } else {
                    EventBus.getDefault().post(wrapper.getEvent());
                }
            }
        });
    }

    static class DiscoveryCardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sdv_img)
        public SimpleDraweeView sdvImage;


        public DiscoveryCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
