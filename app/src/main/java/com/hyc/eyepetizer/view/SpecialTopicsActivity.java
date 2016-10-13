package com.hyc.eyepetizer.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.SpecialTopicsPresenter;
import com.hyc.eyepetizer.view.adapter.ViewAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class SpecialTopicsActivity extends BaseActivity<SpecialTopicsPresenter> implements VideoListContract.View {
    @BindView(R.id.rv_video)
    RecyclerView mRvVideo;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error)
    CustomTextView mTvError;
    @BindView(R.id.rl_error)
    RelativeLayout mRlError;
    private ViewAdapter mAdapter;
    private LinearLayoutManager mManager;

    @Override
    protected void handleIntent() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_special_topics;
    }


    @Override
    public void showList(List<ViewData> datas) {
        if (mAdapter == null) {
            if (mRvVideo.getVisibility()==View.GONE) {
                mRvVideo.setVisibility(View.VISIBLE);
                mRlError.setVisibility(View.GONE);
            }
            mAdapter = new ViewAdapter.Builder(this, datas).build();
            mRvVideo.setAdapter(mAdapter);

        } else {
            mAdapter.addData(datas);
        }
    }

    @Override
    public void showError() {
        mRvVideo.setVisibility(View.GONE);
        mRlError.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMore() {
        mHasMore=false;
    }
    private boolean mHasMore=true;
    private boolean mIsRequesting;
    private RecyclerView.OnScrollListener mOnScrollListener;
    @Override
    protected void initView(){
        mManager=new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mHasMore && !mIsRequesting &&
                        mManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 6) {
                    mPresenter.getMore();
                    mIsRequesting = true;
                }
            }
        };
        mRvVideo.setLayoutManager(mManager);
        mRvVideo.addOnScrollListener(mOnScrollListener);
    }
    @Override
    protected void initPresenterAndData() {
        mPresenter=new SpecialTopicsPresenter(this);
        mPresenter.attachView();
        mPresenter.getVideoList();
    }
}
