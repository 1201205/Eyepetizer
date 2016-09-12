package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.view.adapter.holder.HorizontalVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.VideoViewHolder;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalVideoViewHolder> {
    private List<ViewData> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ItemClickListener listener;
    public HorizontalAdapter(List<ViewData> datas,LayoutInflater context){
        mDatas=datas;
        mLayoutInflater=context;
    }
    @Override
    public HorizontalVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HorizontalVideoViewHolder holder = new HorizontalVideoViewHolder(
            mLayoutInflater.inflate(R.layout.item_video_horizontal, parent, false));
        if (listener != null) {
            holder.setOnItemClickListener(new HorizontalVideoViewHolder.ItemClickListener() {
                @Override public void onItemClicked(int locationY, int p) {
                    listener.onItemClicked(p);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalVideoViewHolder holder, int position) {
        ItemListData data = mDatas.get(position).getData();
        holder.category.setText(
            DataHelper.getCategoryAndDuration(data.getCategory(), data.getDuration()));
        holder.title.setText(data.getTitle());
        holder.img.setImageURI(data.getCover().getDetail());
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }


    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    public interface ItemClickListener {
        void onItemClicked(int myIndex);
    }

}
