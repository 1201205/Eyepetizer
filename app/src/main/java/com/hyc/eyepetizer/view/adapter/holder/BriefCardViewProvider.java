package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.PgcActivity;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;
import com.hyc.eyepetizer.widget.CustomTextView;

public class BriefCardViewProvider extends ItemViewProvider<BriefCardViewProvider.BriefCardViewHolder> {
    @Override
    protected BriefCardViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new BriefCardViewHolder(inflater.inflate(R.layout.item_brief_card, parent, false));
    }

    @Override
    protected void onBindViewHolder(BriefCardViewHolder holder, ViewData data, int position, AdapterParameterWrapper wrapper) {
        final ItemListData itemListData = data.getData();
        FrescoHelper.loadUrl(holder.ico, itemListData.getIcon());
        holder.count.setText(itemListData.getSubTitle());
        holder.name.setText(itemListData.getTitle());
        holder.des.setText(itemListData.getDescription());
        holder.count.setTextColor(wrapper.getColor());
        holder.name.setTextColor(wrapper.getColor());
        holder.des.setTextColor(wrapper.getColor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PgcActivity.start(v.getContext(), itemListData.getTitle(),
                    itemListData.getDescription(),
                        itemListData.getIcon(),
                        DataHelper.getID(itemListData.getActionUrl()));
            }
        });
    }

    static class BriefCardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_count)
        public CustomTextView count;
        @BindView(R.id.tv_name)
        public CustomTextView name;
        @BindView(R.id.tv_des)
        public CustomTextView des;
        @BindView(R.id.sdv_icon)
        public SimpleDraweeView ico;

        public BriefCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            name.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        }
    }
}