package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.widget.CustomTextView;

public class BriefVideoViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_count)
    public CustomTextView count;
    @BindView(R.id.tv_name)
    public CustomTextView name;
    @BindView(R.id.tv_des)
    public CustomTextView des;
    @BindView(R.id.sdv_icon)
    public SimpleDraweeView ico;
    @BindView(R.id.rv_collection)
    public RecyclerView recyclerView;
    public BriefVideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        name.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
    }
}