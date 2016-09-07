package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.VideoReplyContract;
import com.hyc.eyepetizer.model.beans.VideoReply;
import com.hyc.eyepetizer.presenter.VideoReplyPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.VideoReplyAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/7.
 */
public class VideoReplyActivity extends BaseActivity<VideoReplyPresenter> implements VideoReplyContract.View {
    @BindView(R.id.tv_title)
    CustomTextView mTvTitle;
    @BindView(R.id.tv_count)
    CustomTextView mTvCount;
    @BindView(R.id.tv_input)
    CustomTextView mTvInput;
    @BindView(R.id.sdv_blur)
    SimpleDraweeView mSdvBlur;
    @BindView(R.id.rv_reply)
    RecyclerView mRvReply;
    @BindView(R.id.rl_error)
    RelativeLayout mRlError;
    @BindView(R.id.tv_error)
    CustomTextView mTvError;
    private int mID;
    private int mCount;
    private String mUrl;
    private String mTitle;
    private VideoReplyAdapter mAdapter;

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String COUNT = "count";
    private static final String URL = "url";

    public static void start(Context context, int id, int count, String title, String url) {
        Intent intent = new Intent(context, VideoReplyActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(COUNT, count);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent() {
        Intent intent = getIntent();
        mID = intent.getIntExtra(ID, -1);
        mCount = intent.getIntExtra(COUNT, 0);
        mUrl = intent.getStringExtra(URL);
        mTitle = intent.getStringExtra(TITLE);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_reply;
    }

    @Override
    public void showReply(List<VideoReply> replies) {
        if (mAdapter == null) {
            mRlError.setVisibility(View.GONE);
            mAdapter = new VideoReplyAdapter(this);
            mRvReply.setAdapter(mAdapter);
        }
        mAdapter.addReplies(replies);
    }

    @Override
    public void showError() {
        mTvError.setText(R.string.net_error_retry);
        mRlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCount(int count) {
        mTvCount.setText(String.format(AppUtil.getString(R.string.comment_count), count));
    }

    @Override
    public void showNoItem() {
        mTvError.setText(R.string.no_comment);
        mRlError.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initPresenter();
        initView();
    }

    private void initPresenter() {
        mPresenter = new VideoReplyPresenter(this);
        mPresenter.attachView();
        mPresenter.getReply(mID);
    }

    private void initView() {
        FrescoHelper.loadUrl(mSdvBlur, mUrl);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvReply.setLayoutManager(manager);
        showCount(mCount);
        mTvTitle.setText(mTitle);
    }

    @OnClick({R.id.tv_input, R.id.rl_head, R.id.rl_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_input:
                //just 占坑
                break;
            case R.id.rl_head:
                onBackPressed();
                break;
            case R.id.rl_error:
                mPresenter.getReply(mID);
                break;
        }
    }
}
