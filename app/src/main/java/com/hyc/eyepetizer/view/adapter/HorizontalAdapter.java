package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.beans.ItemListData;
import com.hyc.eyepetizer.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.view.adapter.holder.VideoViewHolder;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<ViewData> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public HorizontalAdapter(List<ViewData> datas,LayoutInflater context){
        mDatas=datas;
        mLayoutInflater=context;
    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(
            mLayoutInflater.inflate(R.layout.item_video_horizontal, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
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


}
