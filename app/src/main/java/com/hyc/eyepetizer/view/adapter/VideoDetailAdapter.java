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
    @BindView(R.id.iv_back) ImageView ivBack;
    @BindView(R.id.iv_play) ImageView ivPlay;
    @BindView(R.id.sdv_blur) SimpleDraweeView sdvBlur;
    @BindView(R.id.tv_title) AnimateTextView tvTitle;
    @BindView(R.id.tv_category) AnimateTextView tvCategory;
    @BindView(R.id.iv_more) ImageView ivMore;
    @BindView(R.id.sdv_icon) SimpleDraweeView sdvIcon;
    @BindView(R.id.tv_author_name) CustomTextView tvAuthorName;
    @BindView(R.id.tv_count) CustomTextView tvCount;
    @BindView(R.id.tv_author_des) CustomTextView tvAuthorDes;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.tv_des) AnimateTextView tvDes;
    @BindView(R.id.tv_like_count) CustomTextView tvLikeCount;
    @BindView(R.id.tv_share_count) CustomTextView tvShareCount;
    @BindView(R.id.tv_reply_count) CustomTextView tvReplyCount;
    @BindView(R.id.rl_author) RelativeLayout rlAuthor;
    private List<ViewData> mViewDatas;
    private Context mContext;
    private SparseArray<List<AnimateTextView>> mViews;

    public VideoDetailAdapter(Context context, List<ViewData> datas) {
        mContext = context;
        mViewDatas = datas;
        mViews = new SparseArray<>();
        EventBus.getDefault().register(this);
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
        Log.e("hyc-t1", position + "--");
        List<AnimateTextView> list = null;
        if (mViews.get(position) != null) {
            list = mViews.get(position);
            list.clear();
        } else {
            list = new ArrayList<>();
            mViews.put(position, list);
        }
        ItemListData data = mViewDatas.get(position).getData();
        FrescoHelper.loadUrl(sdvImg, data.getCover().getDetail());
        FrescoHelper.loadUrl(sdvBlur, data.getCover().getBlurred());
        tvTitle.setAnimText(data.getTitle());
        tvCategory.setAnimText(
            DataHelper.getCategoryAndDuration(data.getCategory(), data.getDuration()));
        tvDes.setAnimText(data.getDescription());
        Author author = data.getAuthor();
        if (author != null) {
            FrescoHelper.loadUrl(sdvIcon, author.getIcon());
            tvAuthorName.setText(author.getName());
            tvAuthorDes.setText(author.getDescription());
            tvCount.setText(author.getVideoNum() + "个视频");
        } else {
            rlAuthor.setVisibility(View.GONE);
        }
        tvLikeCount.setText(String.valueOf(data.getConsumption().getCollectionCount()));
        tvReplyCount.setText(String.valueOf(data.getConsumption().getReplyCount()));
        tvShareCount.setText(String.valueOf(data.getConsumption().getShareCount()));
        list.add(tvTitle);
        list.add(tvCategory);
        list.add(tvDes);
        //mViews.add(position,list);
        //tvDes.getViewTreeObserver().addOnPreDrawListener(
        //    new ViewTreeObserver.OnPreDrawListener() {
        //        @Override public boolean onPreDraw() {
        //            tvDes.getViewTreeObserver().removeOnPreDrawListener(this);
        //            tvTitle.animateChar(100);
        //            tvCategory.animateChar(100);
        //            tvDes.animateChar(100);
        //            return true;
        //        }
        //    });
    }


    @Subscribe
    public void handleSelectEvent(VideoSelectEvent event) {
        if (mViews == null || mViews.get(event.position) == null) {
            return;
        }
        List<AnimateTextView> list = mViews.get(event.position);
        for (AnimateTextView view : list) {
            view.animateChar();
        }
    }


    public void unRegister() {
        EventBus.getDefault().unregister(this);
        mViews.clear();
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (mViews.size() <= position) {
            return;
        }
        mViews.get(position).clear();
    }
}
