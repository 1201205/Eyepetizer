package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Space;
import android.widget.TextView;

import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class BlankViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.sp_blank)
    public View space;
    public BlankViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}