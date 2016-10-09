package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyc on 2016/10/9.
 */
public class BlankCardViewHolder extends  RecyclerView.ViewHolder{
    @BindView(R.id.sp_blank)
    public View space;
    public BlankCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
