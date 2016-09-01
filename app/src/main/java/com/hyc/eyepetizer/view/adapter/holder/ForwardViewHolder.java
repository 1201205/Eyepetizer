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
public class ForwardViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_text)
    public CustomTextView textView;
    public ForwardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
