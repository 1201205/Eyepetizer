package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.beans.ViewData;
import com.hyc.eyepetizer.view.adapter.holder.HorizontalViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder> {
    private List<ViewData> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public HorizontalAdapter(List<ViewData> datas,LayoutInflater context){
        mDatas=datas;
        mLayoutInflater=context;
    }
    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HorizontalViewHolder(mLayoutInflater.inflate(R.layout.item_video_horizontal,parent,false));
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        holder.category.setText(mDatas.get(position).getData().getCategory());
        holder.title.setText(mDatas.get(position).getData().getTitle());
        holder.img.setImageURI(mDatas.get(position).getData().getCover().getDetail());
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }


}
