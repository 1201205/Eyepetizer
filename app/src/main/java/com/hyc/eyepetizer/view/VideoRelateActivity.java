package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.VideoRelateContract;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.VideoRelatePresenter;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public class VideoRelateActivity extends BaseActivity<VideoRelatePresenter>
    implements VideoRelateContract.View {
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String URL = "url";
    @BindView(R.id.tv_title) CustomTextView tvTitle;
    @BindView(R.id.iv_back) ImageView ivBack;
    @BindView(R.id.rv_relate) RecyclerView rvRelate;
    @BindView(R.id.sdv_blur) SimpleDraweeView sdvBlur;
    private LinearLayoutManager mManager;
    private TestAdapter mAdapter;
    private int mID;
    private String mTitle;
    private String mUrl;


    public static void start(Context context, int id, String title, String url) {
        Intent intent = new Intent(context, VideoRelateActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }


    @Override protected void handleIntent() {
        mID = getIntent().getIntExtra(ID, 0);
        mTitle = getIntent().getStringExtra(TITLE);
        mUrl = getIntent().getStringExtra(URL);
    }


    @Override protected int getLayoutID() {
        return R.layout.activity_video_relate;
    }


    @Override public void showView(List<ViewData> datas) {
        if (datas == null) {
            return;
        }
        mAdapter = new TestAdapter.Builder(this, datas).setTitleTextColor(Color.WHITE)
            .horizontalItemCilckListener(
                new TestAdapter.HorizontalItemCilckListener() {
                    @Override
                    public void onItemClicked(int parentPosition, int myPosition, int position) {
                        Intent intent =
                            VideoDetailActivity2.newIntent(VideoRelateActivity.this, myPosition,
                                position, mID);
                        startActivity(intent);
                    }
                })
            .build();
        rvRelate.setAdapter(mAdapter);
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        FrescoHelper.loadUrl(sdvBlur, mUrl);
        mPresenter = new VideoRelatePresenter(this);
        mPresenter.attachView();
        mManager = new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRelate.setLayoutManager(mManager);
        tvTitle.setText(mTitle);
        mPresenter.getRelate(mID);
    }


    @Override protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
