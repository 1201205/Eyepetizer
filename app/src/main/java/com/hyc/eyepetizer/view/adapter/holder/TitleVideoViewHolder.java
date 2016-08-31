package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class TitleVideoViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_title)
    public TextView title;
    @BindView(R.id.rv_collection)
    public RecyclerView recyclerView;
    public TitleVideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
