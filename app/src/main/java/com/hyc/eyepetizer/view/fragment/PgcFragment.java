package com.hyc.eyepetizer.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseFragment;
import com.hyc.eyepetizer.contract.PgcContract;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.PgcsPresenter;
import com.hyc.eyepetizer.view.VideoDetailActivity2;
import com.hyc.eyepetizer.view.adapter.ViewAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class PgcFragment extends BaseFragment<PgcsPresenter> implements PgcContract.View {

    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    @BindView(R.id.iv_error)
    ImageView ivError;
    @BindView(R.id.tv_error)
    CustomTextView tvError;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    private ViewAdapter mAdapter;

    private LinearLayoutManager mManager;
    private Unbinder mUnbinder;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private boolean mHasMore=true;
    private boolean mIsRequesting=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        mUnbinder= ButterKnife.bind(this,view);
        initView();
        mPresenter.getPgcs();
        return view;
    }

    private void initView() {
        mManager=new LinearLayoutManager(getContext());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideo.setLayoutManager(mManager);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mHasMore && !mIsRequesting &&
                        mManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 6) {
                    mPresenter.getMorePgcs();
                    mIsRequesting = true;
                }
            }
        };
        rvVideo.addOnScrollListener(mOnScrollListener);
    }

    @Override
    protected void initPresenter() {
        mPresenter=new PgcsPresenter(this);
        mPresenter.attachView();
    }

    @Override
    public void showPgc(List<ViewData> datas) {
        mIsRequesting = false;
        if (mAdapter == null) {
            mAdapter = new ViewAdapter.Builder(getContext(), datas).horizontalItemClickListener(
                new ViewAdapter.HorizontalItemClickListener() {
                    @Override
                    public void onItemClicked(int parentPosition, int myPosition, int position) {
                        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_PGC,
                            getContext(), myPosition, parentPosition);
                        startActivity(intent);
                    }
                }).build();
            rvVideo.setAdapter(mAdapter);
        } else {
            mAdapter.addData(datas);
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void noMore() {
        mHasMore=false;
    }

    @Override
    public void onDestroy() {
        rvVideo.removeOnScrollListener(mOnScrollListener);
        super.onDestroy();
        mUnbinder.unbind();
    }
}
