package com.hyc.eyepetizer.view.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.RouterWrapper;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.AdapterParameterWrapper;
import com.hyc.eyepetizer.view.adapter.ItemViewProvider;
import com.hyc.eyepetizer.widget.CustomTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ForwardViewProvider extends ItemViewProvider<ForwardViewProvider.ForwardViewHolder>{
    @Override
    protected ForwardViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new ForwardViewHolder(inflater.inflate(R.layout.item_text_forward, parent, false));
    }

    @Override
    protected void onBindViewHolder(ForwardViewHolder holder,final ViewData data, int position, AdapterParameterWrapper wrapper) {
        holder.textView.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
        holder.textView.setText(data.getData().getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Context context=v.getContext();
                RouterWrapper wrapper = DataHelper.getIntentByUri(context,
                        data.getData().getActionUrl());
                if (wrapper.getIntent() != null) {
                    context.startActivity(wrapper.getIntent());
                } else {
                    EventBus.getDefault().post(wrapper.getEvent());
                }
            }
        });
    }


    static class ForwardViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_text)
    public CustomTextView textView;
    public ForwardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
}
