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

public class TextHeaderViewProvider extends ItemViewProvider<TextHeaderViewProvider.TextHeaderViewHolder>{
    @Override
    protected TextHeaderViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new TextHeaderViewHolder(inflater.inflate(R.layout.item_text, parent, false));
    }

    @Override
    protected void onBindViewHolder(TextHeaderViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
        holder.head.setText(data.getData().getText());
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
    }


    static class TextHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_head)
        public CustomTextView head;

        public TextHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}