package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyc on 2016/10/9.
 */
public class LeftTextHeaderViewProvider extends ItemViewProvider<LeftTextHeaderViewProvider.LeftTextHeaderViewHolder> {
    @Override
    protected LeftTextHeaderViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new LeftTextHeaderViewHolder(inflater.inflate(R.layout.item_left_text_header, parent, false));
    }

    @Override
    protected void onBindViewHolder(LeftTextHeaderViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        holder.head.setText(data.getData().getText());
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
    }


    public class LeftTextHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_head)
        public TextView head;

        public LeftTextHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
