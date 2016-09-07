package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/7.
 */
public class VideoReplyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.sdv_icon)
    public SimpleDraweeView sdvIcon;
    @BindView(R.id.tv_name)
    public CustomTextView tvName;
    @BindView(R.id.tv_time)
    public CustomTextView tvTime;
    @BindView(R.id.tv_content)
    public CustomTextView tvContent;
    @BindView(R.id.tv_like_count)
    public CustomTextView tvLikeCount;
    @BindView(R.id.tv_hot)
    public CustomTextView tvHot;

    public VideoReplyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
