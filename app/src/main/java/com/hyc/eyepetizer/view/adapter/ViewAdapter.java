package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.view.adapter.holder.ViewModelPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<ViewData> mDatas;
    private AdapterParameterWrapper mWrapper;


    private ViewAdapter(Context context, List<ViewData> datas) {
        mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
        mWrapper = new AdapterParameterWrapper();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return ViewModelPool.getInstance().getProvider(viewType).onCreateViewHolder(mLayoutInflater, parent);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewModelPool.getInstance().getProviderByType(mDatas.get(position).getType()).onBindViewHolder(holder, mDatas.get(position), position, mWrapper);
    }


    @Override
    public int getItemViewType(int position) {
        Log.e("hyc-ii",mDatas.get(position).getType());
        return ViewModelPool.getInstance().getType(mDatas.get(position).getType());
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    public void addData(List<ViewData> datas) {
        int count = getItemCount();
        mDatas.addAll(datas);
        notifyItemRangeInserted(count, datas.size());
    }

    public interface HorizontalItemClickListener {
        //首页需要用到  前面两个参数，其余的需要用到后面两个

        /**
         * @param parentID   对于数据的id
         * @param myPosition 点击位置
         * @param position   点击view（对于父RecyclerView）的位置
         */
        void onItemClicked(int parentID, int myPosition, int position);
    }

    private void setType(int type) {
        mWrapper.setType(type);
    }

    private void setTitleColor(int color) {
        mWrapper.setColor(color);
    }

    private void setFromRank() {
        mWrapper.setFromRank(true);
    }

    public void setListener(ViewAdapter.HorizontalItemClickListener listener) {
        mWrapper.setListener(listener);
    }

    public static class Builder {
        private ViewAdapter adapter;


        public Builder(Context context, List<ViewData> datas) {
            adapter = new ViewAdapter(context, datas);
        }


        public Builder(Context context) {
            adapter = new ViewAdapter(context, new ArrayList<ViewData>());
        }

        public Builder setTitleTextColor(int color) {
            adapter.setTitleColor(color);
            return this;
        }


        public Builder type(int type) {
            adapter.setType(type);
            return this;
        }

        public Builder horizontalItemClickListener(HorizontalItemClickListener listener) {
            adapter.setListener(listener);
            return this;
        }

        public Builder formRank() {
            adapter.setFromRank();
            return this;
        }

        public ViewAdapter build() {
            return adapter;
        }
    }
}
