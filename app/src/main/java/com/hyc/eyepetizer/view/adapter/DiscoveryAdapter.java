package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.WidgetHelper;
import com.hyc.eyepetizer.view.adapter.holder.BannerViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.RectangleCardViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.SquareCardViewHolder;

import java.util.List;

/**
 * Created by ray on 16/9/22.
 */
public class DiscoveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ViewData> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public DiscoveryAdapter(Context context, List<ViewData> datas) {
        mDatas = datas;
        mLayoutInflater=LayoutInflater.from(context);
    }


    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case WidgetHelper.ViewType.HORIZONTAL_SCROLL_CARD:
                return new BannerViewHolder(mLayoutInflater.inflate(R.layout.item_pager,parent,false));
            case WidgetHelper.ViewType.SQUARE_CARD:
                return new SquareCardViewHolder(mLayoutInflater.inflate(R.layout.item_square,parent,false));
            case WidgetHelper.ViewType.RECTANGLE_CARD:
                return new RectangleCardViewHolder(mLayoutInflater.inflate(R.layout.item_rectangle,parent,false));
        }
        return  new SquareCardViewHolder(mLayoutInflater.inflate(R.layout.item_square,parent,false));
    }


    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            bindView((BannerViewHolder) holder,position);
        } else if (holder instanceof SquareCardViewHolder) {
            bindView((SquareCardViewHolder)holder,position);
        } else if (holder instanceof RectangleCardViewHolder) {
            bindView((RectangleCardViewHolder)holder,position);
        }
    }

    private void bindView(BannerViewHolder holder, int position) {
        if (holder.vpBanner.getAdapter()==null) {
            LoopViewPagerAdapter adapter=new LoopViewPagerAdapter(holder.vpBanner,holder.llIndicator);
            adapter.setList(mDatas.get(position).getData().getItemList());
            holder.vpBanner.setAdapter(adapter);
            holder.vpBanner.addOnPageChangeListener(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    private void bindView(SquareCardViewHolder holder, int position) {
        ItemListData data=mDatas.get(position).getData();
        FrescoHelper.loadUrl(holder.img,data.getImage());
        if (data.isShade()) {
            holder.flow.setVisibility(View.VISIBLE);
            holder.flow.setText(data.getTitle());
        } else {
            holder.flow.setVisibility(View.GONE);
        }
    }
    private void bindView(RectangleCardViewHolder holder, int position) {
        FrescoHelper.loadUrl(holder.sdvImage,mDatas.get(position).getData().getImage());
    }


    @Override public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (WidgetHelper.getViewType(mDatas.get(position).getType()));
    }

}
