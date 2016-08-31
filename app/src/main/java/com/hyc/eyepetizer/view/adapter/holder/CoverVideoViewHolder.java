package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class CoverVideoViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.sdv_cover)
    public SimpleDraweeView cover;
    @BindView(R.id.rv_collection)
    public RecyclerView recyclerView;
    public CoverVideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
