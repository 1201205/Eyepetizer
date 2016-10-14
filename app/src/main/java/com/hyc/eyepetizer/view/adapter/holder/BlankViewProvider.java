package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class BlankViewProvider extends ItemViewProvider<BlankViewProvider.BlankViewHolder> {


    @Override
    protected BlankViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new BlankViewHolder(inflater.inflate(R.layout.item_blank, parent, false));
    }

    @Override
    protected void onBindViewHolder(BlankViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.space.getLayoutParams();
        params.height = data.getData().getHeight();
        holder.space.setLayoutParams(params);
    }

    static class BlankViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sp_blank)
        public View space;

        public BlankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}