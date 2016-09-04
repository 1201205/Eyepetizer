package com.hyc.eyepetizer.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.utils.WidgetHelper;
import com.hyc.eyepetizer.view.VideoDetailActivity2;
import com.hyc.eyepetizer.view.adapter.holder.BlankViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.BriefVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.CampaignViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.CoverVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.ForwardViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.NoMoreViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.TextHeaderViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.TitleVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.VideoViewHolder;
import com.hyc.eyepetizer.widget.HorizontalDecoration;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<ViewData> mDatas;

    private int mSpace;
    //建立枚举 2个item 类型

    public TestAdapter(Context context, List<ViewData> datas) {
        this.context = context;
        mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
        mSpace = (int) AppUtil.dip2px(4);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case WidgetHelper.ViewType.VIDEO:
                return new VideoViewHolder(mLayoutInflater.inflate(R.layout.item_video, parent, false));
            case WidgetHelper.ViewType.TEXT_HEADER:
                return new TextHeaderViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
            case WidgetHelper.ViewType.FORWARD_FOOTER:
                return new ForwardViewHolder(mLayoutInflater.inflate(R.layout.item_text_forward, parent, false));
            case WidgetHelper.ViewType.VIDEO_COLLECTION_WITH_COVER:
                return new CoverVideoViewHolder(mLayoutInflater.inflate(R.layout.item_video_collection_cover, parent, false));
            case WidgetHelper.ViewType.VIDEO_COLLECTION_WITH_BRIEF:
                return new BriefVideoViewHolder(mLayoutInflater.inflate(R.layout.item_video_collection_brief, parent, false));
            case WidgetHelper.ViewType.BLANK_FOOTER:
                return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_blank, parent, false));
            case WidgetHelper.ViewType.VIDEO_COLLECTION_WITH_TITLE:
                return new TitleVideoViewHolder(mLayoutInflater.inflate(R.layout.item_video_collection_title, parent, false));
            case WidgetHelper.ViewType.NO_MORE:
                return new NoMoreViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
            case WidgetHelper.ViewType.CAMPAIGN:
                return new CampaignViewHolder(mLayoutInflater.inflate(R.layout.item_campaign, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewHolder) {
            bindView((VideoViewHolder) holder, mDatas.get(position), position);
        } else if (holder instanceof CoverVideoViewHolder) {
            bindView((CoverVideoViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof BriefVideoViewHolder) {
            bindView((BriefVideoViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof TextHeaderViewHolder) {
            bindView((TextHeaderViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof ForwardViewHolder) {
            bindView((ForwardViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof BlankViewHolder) {
            bindView((BlankViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof TitleVideoViewHolder) {
            bindView((TitleVideoViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof NoMoreViewHolder) {
            bindView((NoMoreViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof CampaignViewHolder) {
            bindView((CampaignViewHolder) holder, mDatas.get(position));
        }
    }


    private void bindView(final VideoViewHolder holder, final ViewData data, final int position) {
        final ItemListData itemData = data.getData();
        FrescoHelper.loadUrl(holder.img, itemData.getCover().getDetail());
        holder.setOnItemClickListener(new VideoViewHolder.ItemClickListener() {
            @Override
            public void onItemClicked() {
                // TODO: 16/9/4 先暂时使用当前的shareElement方式  有时间改为正常的方式

                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) context, holder.img, "transition_name");
                Intent intent = VideoDetailActivity2.newIntent(context, data.getIndex(),
                    data.getParentIndex());
                ActivityCompat.startActivity((Activity) context, intent, compat.toBundle());
            }
        });
        holder.title.setText(itemData.getTitle());
        holder.category.setText(DataHelper.getCategoryAndDuration(itemData.getCategory(), itemData.getDuration()));
    }

    private void bindView(CoverVideoViewHolder holder, ViewData data) {
        holder.cover.setImageURI(data.getData().getHeader().getCover());
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(mSpace));
        }
        holder.recyclerView.setAdapter(new HorizontalAdapter(data.getData().getItemList(), mLayoutInflater));
    }

    private void bindView(BriefVideoViewHolder holder, ViewData data) {
        FrescoHelper.loadUrl(holder.ico, data.getData().getHeader().getIco());
        holder.count.setText(data.getData().getHeader().getSubTitle());
        holder.name.setText(data.getData().getHeader().getTitle());
        holder.des.setText(data.getData().getHeader().getDescription());
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(mSpace));
        }
        holder.recyclerView.setAdapter(new HorizontalAdapter(data.getData().getItemList(), mLayoutInflater));
    }

    private void bindView(TextHeaderViewHolder holder, ViewData data) {
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
        holder.head.setText(data.getData().getText());
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
    }

    private void bindView(ForwardViewHolder holder, ViewData data) {
        holder.textView.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
        holder.textView.setText(data.getData().getText());
    }

    private void bindView(NoMoreViewHolder holder, ViewData data) {
        holder.head.setText("- The End -");
    }

    private void bindView(CampaignViewHolder holder, ViewData data) {
        FrescoHelper.loadUrl(holder.campaign, data.getData().getImage());
    }

    private void bindView(BlankViewHolder holder, ViewData data) {
        Log.e("Hyc--", data.getData().getHeight() + "---" + holder.space.getLayoutParams());
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.space.getLayoutParams();
        params.height = data.getData().getHeight();
        holder.space.setLayoutParams(params);
    }

    private void bindView(TitleVideoViewHolder holder, ViewData data) {
        holder.title.setText(data.getData().getHeader().getTitle());
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(mSpace));
        }
        holder.recyclerView.setAdapter(new HorizontalAdapter(data.getData().getItemList(), mLayoutInflater));
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        return (WidgetHelper.getViewType(mDatas.get(position).getType()));
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

}
