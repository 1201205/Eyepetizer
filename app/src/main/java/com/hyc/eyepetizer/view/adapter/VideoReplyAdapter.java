package com.hyc.eyepetizer.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.model.beans.VideoReply;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.holder.NoMoreViewHolder;
import com.hyc.eyepetizer.view.adapter.holder.VideoReplyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class VideoReplyAdapter extends RecyclerView.Adapter {
    public static final int EMPTY = -1;
    private List<VideoReply> mReplies;
    private LayoutInflater mLayoutInflater;

    public VideoReplyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mReplies = new ArrayList<>();
    }

    public void addReplies(List<VideoReply> replies) {
        int count = mReplies.size();
        mReplies.addAll(replies);
        notifyItemInserted(count);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY) {
            return new NoMoreViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
        } else {
            return new VideoReplyViewHolder(mLayoutInflater.inflate(R.layout.item_reply, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoReplyViewHolder) {
            bindHolder((VideoReplyViewHolder) holder, mReplies.get(position));
        } else {
            bindView((NoMoreViewHolder) holder);
        }
    }

    private void bindView(NoMoreViewHolder holder) {
        holder.head.setText(R.string.the_end);
        holder.head.setTextColor(Color.WHITE);
    }


    private void bindHolder(VideoReplyViewHolder holder, VideoReply reply) {
        FrescoHelper.loadUrl(holder.sdvIcon, reply.getUser().getAvatar());
        holder.tvContent.setText(reply.getMessage());
        holder.tvTime.setText(DataHelper.getCommentTime(reply.getCreateTime()));
        if (reply.getHot()) {
            holder.tvHot.setVisibility(View.VISIBLE);
        } else {
            holder.tvHot.setVisibility(View.GONE);
        }
        holder.tvLikeCount.setText(String.valueOf(reply.getLikeCount()));
        holder.tvName.setText(reply.getUser().getNickname());
    }

    @Override
    public int getItemViewType(int position) {
        return mReplies.get(position).getSequence();
    }

    @Override
    public int getItemCount() {
        return mReplies.size();
    }
}
