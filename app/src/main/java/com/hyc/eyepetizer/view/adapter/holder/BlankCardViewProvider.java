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
 * Created by hyc on 2016/10/9.
 */
public class BlankCardViewProvider extends ItemViewProvider<BlankCardViewProvider.BlankCardViewHolder> {
    @Override
    protected BlankCardViewProvider.BlankCardViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new BlankCardViewHolder(inflater.inflate(R.layout.item_blank_card, parent, false));
    }

    @Override
    protected void onBindViewHolder(BlankCardViewProvider.BlankCardViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.space.getLayoutParams();
        params.height = data.getData().getHeight();
        holder.space.setLayoutParams(params);
    }

    static class BlankCardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sp_blank)
        public View space;

        public BlankCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
