package com.hyc.eyepetizer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.beans.ViewData;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {
    private List<ViewData> mDatas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public HorizontalAdapter(List<ViewData> datas,LayoutInflater context){
        mDatas=datas;
        mLayoutInflater=context;
    }
    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_vedio_horizental,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.category.setText(mDatas.get(position).getData().getCategory());
        holder.title.setText(mDatas.get(position).getData().getTitle());
        holder.img.setImageURI(mDatas.get(position).getData().getCover().getDetail());
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
          TextView category;
          TextView title;
          SimpleDraweeView img;
         ViewHolder(View itemView) {
            super(itemView);
            category=(TextView)itemView.findViewById(R.id.tv_category);
            title=(TextView)itemView.findViewById(R.id.tv_title);
            img= (SimpleDraweeView) itemView.findViewById(R.id.sdv_img);
        }
    }
}
