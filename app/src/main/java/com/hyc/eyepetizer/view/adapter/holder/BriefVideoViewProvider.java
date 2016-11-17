package com.hyc.eyepetizer.view.adapter.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.CoverHeader;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.PgcActivity;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.HorizontalAdapter;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.HorizontalDecoration;

public class BriefVideoViewProvider extends ItemViewProvider<BriefVideoViewProvider.BriefVideoViewHolder> {
    @Override
    protected BriefVideoViewProvider.BriefVideoViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new BriefVideoViewHolder(inflater.inflate(R.layout.item_video_collection_brief, parent, false));
    }

    @Override
    protected void onBindViewHolder(BriefVideoViewProvider.BriefVideoViewHolder holder, final ViewData data, final int position, final AdapterParameterWrapper wrapper) {
        final CoverHeader header = data.getData().getHeader();
        final Context context = holder.itemView.getContext();
        holder.rlHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PgcActivity.start(context, header.getTitle(), header.getDescription(),
                        header.getIco(),
                        DataHelper.getID(header.getActionUrl()));
            }
        });
        FrescoHelper.loadUrl(holder.ico, header.getIco());
        holder.count.setText(header.getSubTitle());
        holder.name.setText(header.getTitle());
        holder.des.setText(header.getDescription());
        holder.count.setTextColor(wrapper.getColor());
        holder.name.setTextColor(wrapper.getColor());
        holder.des.setTextColor(wrapper.getColor());
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(context.getResources().getDimensionPixelSize(R.dimen.horizontal_space)));
        }
        HorizontalAdapter adapter = new HorizontalAdapter(data.getData().getItemList(),
                LayoutInflater.from(context));
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


    static class BriefVideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_count)
        public CustomTextView count;
        @BindView(R.id.tv_name)
        public CustomTextView name;
        @BindView(R.id.tv_des)
        public CustomTextView des;
        @BindView(R.id.sdv_icon)
        public SimpleDraweeView ico;
        @BindView(R.id.rv_collection)
        public RecyclerView recyclerView;
        @BindView(R.id.rl_head)
        public RelativeLayout rlHead;

        public BriefVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            name.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        }
    }
}