package com.hyc.eyepetizer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyc.eyepetizer.model.beans.ViewData;

/**
 * Created by Administrator on 2016/10/14.
 */
public abstract class ItemViewProvider<V extends RecyclerView.ViewHolder> {

    protected abstract V onCreateViewHolder(LayoutInflater inflater, ViewGroup parent);

    protected abstract void onBindViewHolder(V holder, ViewData data , int position, AdapterParameterWrapper wrapper);
}
