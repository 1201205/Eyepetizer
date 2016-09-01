package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class NoMoreViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_head)
    public CustomTextView head;
    public NoMoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        head.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.TEMP));
    }
}