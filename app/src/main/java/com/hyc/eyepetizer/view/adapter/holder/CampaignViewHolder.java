package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 广告----
 * Created by Administrator on 2016/8/31.
 */
public class CampaignViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.sdv_campaign)
    public SimpleDraweeView campaign;
    public CampaignViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
