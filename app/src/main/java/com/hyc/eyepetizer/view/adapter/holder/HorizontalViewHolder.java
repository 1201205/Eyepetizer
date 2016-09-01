package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class HorizontalViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_category)
    public CustomTextView category;
    @BindView(R.id.tv_title)
    public CustomTextView title;
    @BindView(R.id.sdv_img)
    public SimpleDraweeView img;
    @BindView(R.id.ll_flow)
    public LinearLayout flow;

    public HorizontalViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
//        flow.setOnTouchListener();
    }
}