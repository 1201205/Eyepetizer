package com.hyc.eyepetizer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseFragment;
import com.hyc.eyepetizer.contract.SelectionContract;
import com.hyc.eyepetizer.event.VideoClickedEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent2;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.SelectionPresenter;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.PullToRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class TestFragment extends BaseFragment<SelectionPresenter>
        implements SelectionContract.View {
    @BindView(R.id.target)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptrf_main)
    PullToRefreshView mRefreshView;
    private Unbinder mUnbinder;
    private TestAdapter mAdapter;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private LinearLayoutManager mManager;
    private boolean mIsRequesting;
    private boolean mHasMore = true;
    private int mTitleHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        mPresenter.attachView();
        mPresenter.getAndShowSelection();
        mRefreshView.setRefreshListener(new PullToRefreshView.RefreshListener() {
            @Override
            public void handleRefresh() {
                onRefresh();
            }
        });
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mHasMore && !mIsRequesting &&
                        mManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 6) {
                    mPresenter.getNextPage();
                    mIsRequesting = true;
                }
            }
        };
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        return root;
    }
    @Subscribe
    public void handleClickEvent(VideoSelectEvent2 event){
        final int p=event.position+mStartPosition;
        mManager.scrollToPosition(p);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v=mRecyclerView.getChildAt(p-mManager.findFirstVisibleItemPosition());
                final int[] l=new int[2];
                v.getLocationInWindow(l);
                mRecyclerView.scrollBy(0,l[1]-getTitleHeight());
            }
        },10);

    }
    private int getTitleHeight(){
        if (mTitleHeight==0) {
            int[] l=new int[2];
            mRecyclerView.getLocationInWindow(l);
            mTitleHeight=l[0];
        }
        return mTitleHeight;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void initPresenter() {
        mPresenter = new SelectionPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        mUnbinder.unbind();
        mPresenter.detachView();
    }


    @Override
    public void showSelection(List<ViewData> datas) {
        mAdapter = new TestAdapter(getContext(), datas);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshView.endAnim();
    }


    @Override
    public void showMoreSelection(List<ViewData> datas) {
        mAdapter.addData(datas);
        mIsRequesting = false;
    }

    @Override
    public void noMore() {
        mHasMore = false;
    }

    @Override
    public void onRefresh() {
        mHasMore = true;
        mPresenter.getAndShowSelection();
    }

    @Override
    public void setNextPushTime(long time) {
        mRefreshView.setNextPushTime(time);
    }

    public int getLastVisiblePosition(){
        return mManager.findLastVisibleItemPosition();
    }
    public int getFirstVisiblePosition(){
        return mManager.findFirstVisibleItemPosition();
    }
    public void scrollToPosition(int position){
        mManager.scrollToPosition(position);
//        Log.e("scrollto",position+"--");
//        View v=mManager.getChildAt(position);
//        int[] l=new int[2];
//        v.getLocationInWindow(l);
//        mRefreshView.scrollBy(0,-l[1]);
    }
    public int getBottom(){
        int[] l=new int[2];
        mRecyclerView.getLocationInWindow(l);
        Log.e("-----",mRecyclerView.getBottom()+"-----"+(l[1]+mRefreshView.getHeight()));
        return l[1]+mRefreshView.getHeight();
    }
    public void setStartPosition(int p){
        mStartPosition=p;
    }
    private int mStartPosition;
}
