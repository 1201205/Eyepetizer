package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BriefVideoViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_count)
    public TextView count;
    @BindView(R.id.tv_name)
    public TextView name;
    @BindView(R.id.tv_des)
    public TextView des;
    @BindView(R.id.sdv_icon)
    public SimpleDraweeView ico;
    @BindView(R.id.rv_collection)
    public RecyclerView recyclerView;
    public BriefVideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}