package com.hyc.eyepetizer.view.fragment;

import android.content.Intent;
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
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseFragment;
import com.hyc.eyepetizer.contract.SelectionContract;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.SectionModel;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.SelectionPresenter;
import com.hyc.eyepetizer.view.VideoDetailActivity2;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.LoadingAnimView;
import com.hyc.eyepetizer.widget.PullToRefreshView;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/8/30.
 */
public class TestFragment extends BaseFragment<SelectionPresenter>
        implements SelectionContract.View {
    @BindView(R.id.target)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptrf_main)
    PullToRefreshView mRefreshView;
    @BindView(R.id.loading)
    LoadingAnimView mLoadingView;
    private Unbinder mUnbinder;
    private TestAdapter mAdapter;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private LinearLayoutManager mManager;
    private boolean mIsRequesting;
    private boolean mHasMore = true;
    private int mTitleHeight;
    private int mStartPosition;
    private int mLastIndex;


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
    public void handleSelectEvent(VideoSelectEvent event) {
        if (event.fromType != FromType.TYPE_MAIN || mLastIndex == event.position) {
            return;
        }
        mLastIndex=event.position;
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
        }, 5);
    }


    private int getTitleHeight(){
        if (mTitleHeight==0) {
            int[] l=new int[2];
            mRecyclerView.getLocationInWindow(l);
            mTitleHeight = l[1];
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
        mRecyclerView.clearOnScrollListeners();
        mUnbinder.unbind();
        mPresenter.detachView();
    }


    @Override
    public void showSelection(List<ViewData> datas) {
        mAdapter = new TestAdapter.Builder(getContext(), datas).type(FromType.TYPE_MAIN)
            .horizontalItemClickListener(
            new TestAdapter.HorizontalItemClickListener() {
                @Override
                public void onItemClicked(int parentPosition, int myPosition, int position) {
                    Log.e("datgetTypea",parentPosition+"----"+myPosition);
                  Intent intent= VideoDetailActivity2.newIntent(FromType.TYPE_SECTION,getContext(),myPosition,parentPosition);
                    startActivity(intent);
                }
            }).build();
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

    public void setStartPosition(int p){
        mStartPosition=p;
    }


    public boolean isLoading() {
        return mLoadingView.isLoading();
    }
}
