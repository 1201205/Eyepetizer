package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hyc.eyepetizer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyc on 2016/10/9.
 */
public class LeftTextHeaderViewHolder extends  RecyclerView.ViewHolder{
    @BindView(R.id.tv_head)
    public TextView head;
    public LeftTextHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
