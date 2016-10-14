package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;
import com.hyc.eyepetizer.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class NoMoreViewProvider extends ItemViewProvider<NoMoreViewProvider.NoMoreViewHolder> {
    @Override
    protected NoMoreViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new NoMoreViewHolder(inflater.inflate(R.layout.item_no_more, parent, false));
    }

    @Override
    protected void onBindViewHolder(NoMoreViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        holder.head.setTextColor(wrapper.getColor());
    }

    static class NoMoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_head)
        public CustomTextView head;

        public NoMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            head.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.LOBSTER));
        }
    }
}