package com.hyc.eyepetizer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.VideoListPresenter;
import com.hyc.eyepetizer.presenter.VideoListPresenterGenerator;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class VideoListFragment extends BaseFragment<VideoListPresenter>
    implements VideoListContract.View {
    private static final String FROM_TYPE = "from_type";
    private static final String TAG = "tag";
    private static final String ID = "id";
    private static final String HASMORE = "has_more";
    @BindView(R.id.rv_video)
    RecyclerView mRvVideo;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error)
    CustomTextView mTvError;
    @BindView(R.id.rl_error)
    RelativeLayout mRlError;
    private int mID;
    private int mType;
    private String mTag;
    private boolean mHasMore;
    private LinearLayoutManager mManager;
    private TestAdapter mAdapter;
    private Unbinder mUnBinder;
    private int mTop;
    private int mLastIndex;
    private boolean mIsRequesting;
    private RecyclerView.OnScrollListener mOnScrollListener;


    public VideoListFragment() {}


    public static VideoListFragment instantiate(int fromType, String tag) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FROM_TYPE, fromType);
        bundle.putString(TAG, tag);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static VideoListFragment instantiate(int fromType, String tag, int id,boolean hasMore) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FROM_TYPE, fromType);
        bundle.putString(TAG, tag);
        bundle.putInt(ID, id);
        bundle.putBoolean(HASMORE, hasMore);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initPresenter() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTag = bundle.getString(TAG);
            mType = bundle.getInt(FROM_TYPE);
            mID = bundle.getInt(ID);
            mHasMore=bundle.getBoolean(HASMORE,false);
        }
        mPresenter = VideoListPresenterGenerator.generate(mType, mTag, mID, this);
        mPresenter.attachView();
    }


    public void scrollTo(int index) {
        if (mLastIndex == index) {
            return;
        }
        mLastIndex = index;
        mManager.scrollToPosition(mLastIndex);
        mRvVideo.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v = mRvVideo.getChildAt(mLastIndex - mManager.findFirstVisibleItemPosition());
                final int[] l = new int[2];
                v.getLocationInWindow(l);
                mRvVideo.scrollBy(0, l[1] - mTop);
            }
        }, 5);
    }


    public void setLastIndex(int index){
        mLastIndex=index;
    }


    public void scrollTo(int index,final int y) {
        if (mLastIndex == index) {
            return;
        }
        mLastIndex = index;

        mManager.scrollToPosition(mLastIndex);
        mRvVideo.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v = mRvVideo.getChildAt(mLastIndex - mManager.findFirstVisibleItemPosition());
                final int[] l = new int[2];
                v.getLocationInWindow(l);
                mRvVideo.scrollBy(0, l[1] - mTop-y);
            }
        }, 5);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        mManager = new LinearLayoutManager(getContext());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvVideo.setLayoutManager(mManager);
        if (mHasMore) {
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
            mRvVideo.addOnScrollListener(mOnScrollListener);
        }
        //45+36
        mTop = (int) (AppUtil.dip2px(81) + AppUtil.getStatusBarHeight(getContext()));
        mPresenter.getVideoList();
        return view;
    }


    @Override public void onDestroy() {
        mRvVideo.removeOnScrollListener(mOnScrollListener);
        mPresenter.detachView();
        mUnBinder.unbind();
        super.onDestroy();
    }


    @Override
    public void showList(List<ViewData> datas) {
        mIsRequesting=false;
        if (mAdapter == null) {
            mRlError.setVisibility(View.GONE);
            mAdapter = new TestAdapter.Builder(getContext(), datas).formRank().type(mType).build();
            mRvVideo.setAdapter(mAdapter);
        } else {
            mAdapter.addData(datas);
        }
    }


    @Override
    public void showError() {
        mRlError.setVisibility(View.VISIBLE);
        mRlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getVideoList();
            }
        });
    }

    @Override
    public void noMore() {
        mHasMore=false;
    }
}
