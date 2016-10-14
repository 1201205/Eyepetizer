package com.hyc.eyepetizer.view.adapter.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
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
public class VideoViewProvider extends ItemViewProvider<VideoViewProvider.VideoViewHolder> {


    @Override
    protected VideoViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new VideoViewHolder(inflater.inflate(R.layout.item_video, parent, false));
    }

    @Override
    protected void onBindViewHolder(VideoViewHolder holder, final ViewData viewData, final int position, final AdapterParameterWrapper wrapper) {
        final ItemListData itemData = viewData.getData();
        FrescoHelper.loadUrl(holder.img, itemData.getCover().getDetail());
        holder.setOnItemClickListener(new VideoViewProvider.VideoViewHolder.ItemClickListener() {
            @Override
            public void onItemClicked(int locationY, int p) {
                EventBus.getDefault()
                        .post(
                                new StartVideoDetailEvent(wrapper.getType(), locationY, viewData.getParentIndex(),
                                        viewData.getIndex(),
                                        itemData.getCover().getDetail(), position));
            }
        });
        if (itemData.getLabel() != null) {
            holder.label.setText(itemData.getLabel().getText());
        } else {
            holder.label.setText(null);
        }
        if (wrapper.isFromRank()) {
            holder.rank.setVisibility(View.VISIBLE);
            holder.rank.setText((position + 1) + ".");
        }
        holder.title.setText(itemData.getTitle());
        holder.category.setText(DataHelper.getCategoryAndDuration(itemData.getCategory(), itemData.getDuration()));
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category)
        public CustomTextView category;
        @BindView(R.id.tv_title)
        public CustomTextView title;
        @BindView(R.id.sdv_img)
        public SimpleDraweeView img;
        @BindView(R.id.rl_flow)
        public RelativeLayout flow;
        @BindView(R.id.tv_label)
        public CustomTextView label;
        @BindView(R.id.tv_rank)
        public CustomTextView rank;

        private Animator mAnimator;
        private float mLastX;
        private float mLastY;
        private float mMask;
        private ItemClickListener mItemClickListener;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
            mMask = ViewConfiguration.getTouchSlop();
            mAnimator = ObjectAnimator.ofFloat(flow, "alpha", 0);
            mAnimator.setDuration(500);
            flow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("hyc---test1", motionEvent.getAction() + "");
                    //                Log.e("hyc-TouchEvent","--flow--"+motionEvent.getAction());
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mAnimator.start();
                            mLastX = motionEvent.getX();
                            mLastY = motionEvent.getY();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (Math.abs(motionEvent.getX() - mLastX) > mMask || Math.abs(motionEvent.getY() - mLastY) > mMask) {
                                reset();
                            } else {
                                mLastX = motionEvent.getX();
                                mLastY = motionEvent.getY();
                            }
                            return false;
                        case MotionEvent.ACTION_UP:
                            reset();
                            if (mItemClickListener != null) {
                                int[] lo = new int[2];
                                img.getLocationInWindow(lo);
                                mItemClickListener.onItemClicked(lo[1], getAdapterPosition());
                            }
                            return false;
                        case MotionEvent.ACTION_CANCEL:
                            reset();
                            return false;

                    }
                    return false;
                }
            });
        }

        public void setOnItemClickListener(ItemClickListener listener) {
            mItemClickListener = listener;
        }


        private void reset() {
            if (mAnimator.isRunning()) {
                mAnimator.end();
            }
            flow.setAlpha(1);
        }

        public interface ItemClickListener {
            void onItemClicked(int locationY, int position);
        }
    }


}
