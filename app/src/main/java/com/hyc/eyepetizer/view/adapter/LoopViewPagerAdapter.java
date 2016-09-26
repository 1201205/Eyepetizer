package com.hyc.eyepetizer.view.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import java.util.ArrayList;
import java.util.List;

public class LoopViewPagerAdapter extends BaseLoopPagerAdapter {
    private final List<ViewData> mDatas;
    private final ViewGroup mIndicators;
    private ItemClickListener mItemClickListener;
    private int mLastPosition;


    public LoopViewPagerAdapter(ViewPager viewPager, ViewGroup indicators) {
        super(viewPager);
        mIndicators = indicators;
        mDatas = new ArrayList<>();
    }

    public void setList(List<ViewData> datas){
        mDatas.clear();
        mDatas.addAll(datas);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    /**
     * oh shit! An indicator view is badly needed!
     * this shit have no animation at all.
     */
    private void initIndicators() {
        if (mIndicators.getChildCount() != mDatas.size() && mDatas.size() > 1) {
            mIndicators.removeAllViews();
            Resources res = mIndicators.getResources();
            int size = (int) AppUtil.dip2px(6);
            int margin = size;
            for (int i = 0; i < getPagerCount(); i++) {
                ImageView indicator = new ImageView(mIndicators.getContext());
                indicator.setAlpha(240);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
                lp.setMargins(margin, margin, 0, margin);
                lp.gravity = Gravity.CENTER;
                indicator.setLayoutParams(lp);
                Drawable drawable = res.getDrawable(R.drawable.selector_indicator);
                indicator.setImageDrawable(drawable);
                mIndicators.addView(indicator);
            }
        }
    }


    @Override
    public void notifyDataSetChanged() {
        initIndicators();
        super.notifyDataSetChanged();
    }


    @Override
    public int getPagerCount() {
        return mDatas.size();
    }


    @Override
    public ViewData getItem(int position) {
        return mDatas.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_img, parent, false);
            holder = new ViewHolder();
            holder.sdvBanner = (SimpleDraweeView) convertView.findViewById(R.id.sdv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ViewData data = mDatas.get(position);
        if (mItemClickListener != null) {
            holder.sdvBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.itemClicked(data);
                }
            });
        }

        FrescoHelper.loadUrl(holder.sdvBanner, data.getData().getImage());
        return convertView;
    }


    @Override
    public void onPageItemSelected(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mIndicators.getChildAt(mLastPosition).setActivated(false);
            mIndicators.getChildAt(position).setActivated(true);
        }
        mLastPosition = position;
    }


    public interface ItemClickListener {
        void itemClicked(ViewData data);
    }


    public static class ViewHolder {
        SimpleDraweeView sdvBanner;
    }
}
