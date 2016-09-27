package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.beans.CoverHeader;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.utils.WidgetHelper;
import com.hyc.eyepetizer.view.PagerListActivity;
import com.hyc.eyepetizer.view.PgcActivity;
import com.hyc.eyepetizer.view.RankActivity;
import com.hyc.eyepetizer.view.SelectionActivity;
import com.hyc.eyepetizer.view.adapter.holder.BlankViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.BriefCardViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.BriefVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.CampaignViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.CoverVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.ForwardViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.NoMoreViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.RectangleCardViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.TextHeaderViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.TitleVideoViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.VideoViewHolder;
import com.hyc.eyepetizer.widget.HorizontalDecoration;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/8/26.
 */
public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<ViewData> mDatas;

    private int mSpace;
    private HorizontalItemClickListener mHorizontalItemClickListener;
    //视频相关  白色
    private int mTitleColor = AppUtil.getColor(R.color.title_black);
    private int mType;
    private boolean formRank;


    private TestAdapter(Context context, List<ViewData> datas) {
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
            case WidgetHelper.ViewType.BRIEF_CARD:
                return new BriefCardViewHolder(mLayoutInflater.inflate(R.layout.item_brief_card, parent, false));
            case WidgetHelper.ViewType.BANNER2:
                return new RectangleCardViewHolder(
                    mLayoutInflater.inflate(R.layout.item_img, parent, false));
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
            bindView((BriefVideoViewHolder) holder, mDatas.get(position), position);
        } else if (holder instanceof TextHeaderViewHolder) {
            bindView((TextHeaderViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof ForwardViewHolder) {
            bindView((ForwardViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof BlankViewHolder) {
            bindView((BlankViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof TitleVideoViewHolder) {
            bindView((TitleVideoViewHolder) holder, mDatas.get(position), position);
        } else if (holder instanceof NoMoreViewHolder) {
            bindView((NoMoreViewHolder) holder);
        } else if (holder instanceof CampaignViewHolder) {
            bindView((CampaignViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof BriefCardViewHolder) {
            bindView((BriefCardViewHolder) holder, mDatas.get(position));
        } else if (holder instanceof RectangleCardViewHolder) {
            bindView((RectangleCardViewHolder) holder, mDatas.get(position));
        }
    }


    private void bindView(final VideoViewHolder holder, final ViewData data, final int position) {
        final ItemListData itemData = data.getData();
        FrescoHelper.loadUrl(holder.img, itemData.getCover().getDetail());
        holder.setOnItemClickListener(new VideoViewHolder.ItemClickListener() {
            @Override
            public void onItemClicked(int locationY, int p) {
                EventBus.getDefault()
                        .post(
                                new StartVideoDetailEvent(mType, locationY, data.getParentIndex(),
                                        data.getIndex(),
                                        itemData.getCover().getDetail(), position));


                // TODO: 16/9/4 先暂时使用当前的shareElement方式  有时间改为正常的方式

//                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    (Activity) context, holder.img, "transition_name");
//                Intent intent = VideoDetailActivity2.newIntent(context, data.getIndex(),
//                    data.getParentIndex());
//                ActivityCompat.startActivity((Activity) context, intent, compat.toBundle());
            }
        });
        if (itemData.getLabel() != null) {
            holder.label.setText(itemData.getLabel().getText());
        } else {
            holder.label.setText(null);
        }
        if (formRank) {
            holder.rank.setVisibility(View.VISIBLE);
            holder.rank.setText((position + 1) + ".");
        }
        holder.title.setText(itemData.getTitle());
        holder.category.setText(DataHelper.getCategoryAndDuration(itemData.getCategory(), itemData.getDuration()));
    }


    private void bindView(CoverVideoViewHolder holder, final ViewData data) {
        holder.cover.setImageURI(data.getData().getHeader().getCover());
        holder.cover.setOnClickListener(getCoveOnClickListener(data.getData().getHeader()));
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(mSpace));
        }
        HorizontalAdapter adapter = new HorizontalAdapter(data.getData().getItemList(),
                mLayoutInflater);
        if (mHorizontalItemClickListener != null) {
            adapter.setOnItemClickListener(new HorizontalAdapter.ItemClickListener() {
                @Override
                public void onItemClicked(int myIndex) {
                    mHorizontalItemClickListener.onItemClicked(data.getData().getHeader().getId(), myIndex,
                            0);
                }
            });
        }
        holder.recyclerView.setAdapter(adapter);
    }

    private void bindView(BriefVideoViewHolder holder, final ViewData data, final int position) {
        final CoverHeader header = data.getData().getHeader();

        holder.rlHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PgcActivity.start(context, header.getTitle(), header.getDescription(),
                        header.getIco(),
                        DataHelper.getID(header.getActionUrl()));
            }
        });
        FrescoHelper.loadUrl(holder.ico, header.getIco());
        holder.count.setText(header.getSubTitle());
        holder.name.setText(header.getTitle());
        holder.des.setText(header.getDescription());
        holder.count.setTextColor(mTitleColor);
        holder.name.setTextColor(mTitleColor);
        holder.des.setTextColor(mTitleColor);
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(mSpace));
        }
        HorizontalAdapter adapter = new HorizontalAdapter(data.getData().getItemList(),
                mLayoutInflater);
        if (mHorizontalItemClickListener != null) {
            adapter.setOnItemClickListener(new HorizontalAdapter.ItemClickListener() {
                @Override
                public void onItemClicked(int myIndex) {
                    mHorizontalItemClickListener.onItemClicked(data.getData().getHeader().getId(), myIndex,
                            position);
                }
            });
        }
        holder.recyclerView.setAdapter(adapter);
    }


    private void bindView(BriefCardViewHolder holder, final ViewData data) {
        FrescoHelper.loadUrl(holder.ico, data.getData().getIcon());
        holder.count.setText(data.getData().getSubTitle());
        holder.name.setText(data.getData().getTitle());
        holder.des.setText(data.getData().getDescription());
        holder.count.setTextColor(mTitleColor);
        holder.name.setTextColor(mTitleColor);
        holder.des.setTextColor(mTitleColor);
    }


    private void bindView(TextHeaderViewHolder holder, ViewData data) {
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
        holder.head.setText(data.getData().getText());
        holder.head.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
    }


    private void bindView(ForwardViewHolder holder, ViewData data) {
        holder.textView.setTypeface(TypefaceHelper.getTypeface(data.getData().getFont()));
        holder.textView.setText(data.getData().getText());
        if ("eyepetizer://feed/".equals(data.getData().getActionUrl())) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SelectionActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }


    private void bindView(NoMoreViewHolder holder) {
        holder.head.setText(R.string.the_end);
        holder.head.setTextColor(mTitleColor);
    }


    private void bindView(CampaignViewHolder holder, ViewData data) {
        FrescoHelper.loadUrl(holder.campaign, data.getData().getImage());
    }


    private void bindView(BlankViewHolder holder, ViewData data) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.space.getLayoutParams();
        params.height = data.getData().getHeight();
        holder.space.setLayoutParams(params);
    }
    private void bindView(RectangleCardViewHolder holder, final ViewData data) {

                FrescoHelper.loadUrl(holder.sdvImage,data.getData().getImage());
        holder.sdvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              context.startActivity(DataHelper.getIntentByUri(context,data.getData().getActionUrl()));
            }
        });
    }

    private void bindView(TitleVideoViewHolder holder, final ViewData data, final int position) {
        holder.title.setTextColor(mTitleColor);
        holder.title.setText(data.getData().getHeader().getTitle());
        holder.title.setOnClickListener(getCoveOnClickListener(data.getData().getHeader()));
        if (holder.recyclerView.getLayoutManager() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerView.setLayoutManager(manager);
            holder.recyclerView.addItemDecoration(new HorizontalDecoration(mSpace));
        }

        HorizontalAdapter adapter = new HorizontalAdapter(data.getData().getItemList(),
                mLayoutInflater);
        if (mHorizontalItemClickListener != null) {
            adapter.setOnItemClickListener(new HorizontalAdapter.ItemClickListener() {
                @Override
                public void onItemClicked(int myIndex) {
                    mHorizontalItemClickListener.onItemClicked(data.getData().getHeader().getId(), myIndex,
                            position);
                }
            });
        }
        holder.recyclerView.setAdapter(adapter);
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


    public ViewData getDataByIndex(int index) {
        return mDatas.get(index);
    }

    private View.OnClickListener getCoveOnClickListener(final CoverHeader header) {
        //// TODO: 2016/9/21   对uri进行解析
        String url = header.getActionUrl();
        if ("eyepetizer://ranklist/".equals(url)) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RankActivity.class);
                    context.startActivity(intent);
                }
            };
        } else if (url.contains("eyepetizer://tag/")) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PagerListActivity.start(context, header.getTitle(), header.getId(),
                        FromType.TYPE_TAG_DATE, FromType.TYPE_TAG_SHARE, true);
                }
            };
        } else if (url.contains("eyepetizer://category/")) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PagerListActivity.start(context, header.getTitle(), header.getId(), FromType.TYPE_CATEGORY_DATE, FromType.TYPE_CATEGORY_SHARE, true);
                }
            };
        }
        return null;
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


    public static class Builder {
        private TestAdapter adapter;


        public Builder(Context context, List<ViewData> datas) {
            adapter = new TestAdapter(context, datas);
        }


        public Builder(Context context) {
            adapter = new TestAdapter(context, new ArrayList<ViewData>());
        }

        public Builder setTitleTextColor(int color) {
            adapter.mTitleColor = color;
            return this;
        }


        public Builder type(int type) {
            adapter.mType = type;
            return this;
        }

        public Builder horizontalItemClickListener(HorizontalItemClickListener listener) {
            adapter.mHorizontalItemClickListener = listener;
            return this;
        }

        public Builder formRank() {
            adapter.formRank = true;
            return this;
        }

        public TestAdapter build() {
            return adapter;
        }
    }
}
