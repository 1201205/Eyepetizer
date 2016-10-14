package com.hyc.eyepetizer.view.adapter.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.RouterWrapper;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.HorizontalAdapter;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.HorizontalDecoration;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class TitleVideoViewProvider extends ItemViewProvider<TitleVideoViewProvider.TitleVideoViewHolder>{
    @Override
    protected TitleVideoViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new TitleVideoViewHolder(inflater.inflate(R.layout.item_video_collection_title, parent, false));
    }

    @Override
    protected void onBindViewHolder(TitleVideoViewHolder holder, final ViewData data, final int position, final AdapterParameterWrapper wrapper) {
        final Context context=holder.itemView.getContext();
        holder.title.setTextColor(wrapper.getColor());
        holder.title.setText(data.getData().getHeader().getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                RouterWrapper wrapper = DataHelper.getIntentByUri(context,
                        data.getData().getHeader().getActionUrl());
                if (wrapper.getIntent() != null) {
                    context.startActivity(wrapper.getIntent());
                } else {
                    EventBus.getDefault().post(wrapper.getEvent());
                }
            }
        });
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(context.getResources().getDimensionPixelSize(R.dimen.horizontal_space)));
        }

        HorizontalAdapter adapter = new HorizontalAdapter(data.getData().getItemList(),
               LayoutInflater.from( holder.recyclerView.getContext()));
        if (wrapper.getListener() != null) {
            adapter.setOnItemClickListener(new HorizontalAdapter.ItemClickListener() {
                @Override
                public void onItemClicked(int myIndex) {
                    wrapper.getListener().onItemClicked(data.getData().getHeader().getId(), myIndex,
                            position);
                }
            });
        }
        holder.recyclerView.setAdapter(adapter);
    }

    static class TitleVideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        public CustomTextView title;
        @BindView(R.id.rv_collection)
        public RecyclerView recyclerView;

        public TitleVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
