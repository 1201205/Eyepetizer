package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BriefCardViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_count)
    public CustomTextView count;
    @BindView(R.id.tv_name)
    public CustomTextView name;
    @BindView(R.id.tv_des)
    public CustomTextView des;
    @BindView(R.id.sdv_icon)
    public SimpleDraweeView ico;
    public BriefCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        name.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
    }
}