package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.beans.Author;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.widget.AnimateTextView;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ray on 16/9/4.
 */
public class VideoDetailAdapter extends PagerAdapter {
    @BindView(R.id.sdv_img) SimpleDraweeView sdvImg;
    @BindView(R.id.sdv_blur) SimpleDraweeView sdvBlur;
    private List<ViewData> mViewDatas;
    private Context mContext;

    public VideoDetailAdapter(Context context, List<ViewData> datas) {
        mContext = context;
        mViewDatas = datas;
    }


    @Override public int getCount() {
        return mViewDatas == null ? 0 : mViewDatas.size();
    }


    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_video_detail, container, false);
        ButterKnife.bind(this, view);
        initView(position);
        container.addView(view);
        return view;
    }


    private void initView(int position) {
        ItemListData data = mViewDatas.get(position).getData();
        FrescoHelper.loadUrl(sdvImg, data.getCover().getDetail());
        FrescoHelper.loadUrl(sdvBlur, data.getCover().getBlurred());
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
