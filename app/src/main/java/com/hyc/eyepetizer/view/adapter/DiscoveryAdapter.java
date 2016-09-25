package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.WidgetHelper;
import com.hyc.eyepetizer.view.adapter.holder.BannerViewHolder;
import java.util.List;

/**
 * Created by ray on 16/9/22.
 */
public class DiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ViewData> mDatas;
    private Context mContext;


    public DiscoveryAdapter(Context context, List<ViewData> datas) {
        mDatas = datas;
        mContext = context;
    }


    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case WidgetHelper.ViewType.HORIZONTAL_SCROLL_CARD:
                return new BannerViewHolder()
        }
        return null;
    }


    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (WidgetHelper.getViewType(mDatas.get(position).getType()));
    }

}
