package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ray on 16/10/7.
 */

public class RecommendsAdapter extends BaseAdapter {
    private List<ViewData> mDatas;
    private Context mContext;


    public RecommendsAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
    }


    @Override public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    @Override public ViewData getItem(int position) {
        return mDatas.get(position);
    }


    @Override public long getItemId(int position) {
        return position;
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recommends, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemListData data = mDatas.get(position).getData();
        FrescoHelper.loadUrl(holder.sdvImg, data.getCover().getDetail());
        FrescoHelper.loadUrl(holder.sdvBlur, data.getCover().getBlurred());
        holder.tvDes.setText(data.getDescription());
        holder.tvTitle.setText(data.getTitle());
        holder.tvCategory.setText(
            DataHelper.getCategoryAndDuration(data.getCategory(), data.getDuration()));

        return convertView;
    }


    public void reAdd(ViewData data) {
        mDatas.add(0, data);
        notifyDataSetChanged();
    }


    public void remove(int index) {
        if (index > -1 && index < mDatas.size()) {
            mDatas.remove(index);
            notifyDataSetChanged();
        }
    }


    public void addAll(Collection<ViewData> collection) {
        if (mDatas.size() == 0) {
            mDatas.addAll(collection);
            notifyDataSetChanged();
        } else {
            mDatas.addAll(collection);
        }
    }


    static class ViewHolder {
        @BindView(R.id.sdv_img) SimpleDraweeView sdvImg;
        @BindView(R.id.tv_title) CustomTextView tvTitle;
        @BindView(R.id.tv_category) CustomTextView tvCategory;
        @BindView(R.id.rl_flow) RelativeLayout rlFlow;
        @BindView(R.id.sdv_below_blur) SimpleDraweeView sdvBlur;
        @BindView(R.id.tv_des) CustomTextView tvDes;


        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}
