package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.DailySelectionContract;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.DailySelectionModel;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.DailySelectionPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.LoadingAnimView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import com.hyc.eyepetizer.widget.PullToRefreshView;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/9/8.
 */
public class SelectionActivity extends BaseActivity<DailySelectionPresenter> implements
    DailySelectionContract.View {
    private static final long ANIMTION_DURATION = 350;
    @BindView(R.id.tv_remain) CustomTextView tvRemain;
    @BindView(R.id.loading) LoadingAnimView loading;
    @BindView(R.id.head) LinearLayout head;
    @BindView(R.id.target) RecyclerView target;
    @BindView(R.id.ptrf_main) PullToRefreshView ptrfMain;
    @BindView(R.id.sdv_anim) SimpleDraweeView sdvAnim;
    @BindView(R.id.fl_all) FrameLayout flAll;
    private TestAdapter mAdapter;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private boolean hasMore = true;
    private boolean isRequesting;
    private float mItemHeight;
    private float mTitleHeight;
    private float mRatio;
    private int lastY;
    private int mEndY;
    private AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private MyAnimatorListener mListener = new MyAnimatorListener() {
        @Override public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };
    private int mLastIndex;
    private SparseArray<Integer> mMap;
    private LinearLayoutManager mManager;
    private int mStatusBarHeight;


    @Override protected void handleIntent() {
    }


    @Override protected int getLayoutID() {
        return R.layout.activity_daily_selection;
    }


    @Override public void showSelection(List<ViewData> datas, boolean hasMore) {
        if (mAdapter == null) {
            TestAdapter.Builder builder = new TestAdapter.Builder(this);
            mAdapter = builder.type(FromType.TYPE_DAILY).build();
            target.setAdapter(mAdapter);
        }
        isRequesting = false;
        mAdapter.addData(datas);
    }


    @Subscribe
    public void handleSelectEvent(VideoSelectEvent event) {
        if (mLastIndex == event.position) {
            return;
        }
        if (mMap == null) {
            mMap = DailySelectionModel.getInstance().getMap();
        }
        mLastIndex = event.position;
        final int p = mMap.get(event.position);

        Log.e("jjs", event.position + "-----" + p);
        mManager.scrollToPosition(p);
        target.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v = target.getChildAt(p - mManager.findFirstVisibleItemPosition());
                final int[] l = new int[2];
                v.getLocationInWindow(l);
                target.scrollBy(0, (int) (l[1] - mTitleHeight - getStatusBarHeight()));
            }
        }, 25);
    }


    @Override public void showError() {

    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initPresenter();
        initView();
    }


    private void initView() {
        mTitleHeight = AppUtil.dip2px(45);
        mItemHeight = AppUtil.dip2px(250);
        mRatio = AppUtil.dip2px(353) / mItemHeight;
        mManager = new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (hasMore && !isRequesting &&
                    mManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 6) {
                    mPresenter.getMoreDailySelection();
                    isRequesting = true;
                }
            }
        };
        target.setLayoutManager(mManager);
        target.addOnScrollListener(mOnScrollListener);
    }


    private void initPresenter() {
        mPresenter = new DailySelectionPresenter(this);
        mPresenter.attachView();
        mPresenter.getDailySelection();
    }


    @Subscribe
    public void handleStartActivity(final StartVideoDetailEvent event) {
        if (event.fromType != FromType.TYPE_DAILY || loading.isLoading()) {
            return;
        }
        //EventBus.getDefault().post(new VideoSelectEvent(FromType.TYPE_DAILY,DailySelectionModel.getInstance().getMap().indexOfValue(event.position)));
        sdvAnim.setVisibility(View.VISIBLE);
        sdvAnim.setY(event.locationY - getStatusBarHeight());
        FrescoHelper.loadUrl(sdvAnim, event.url);
        lastY = event.locationY;
        sdvAnim.animate()
            .scaleX(mRatio)
            .scaleY(mRatio)
            .y((mItemHeight * (mRatio - 1) / 2))
            .setDuration(500)
            .setListener(new MyAnimatorListener() {
                @Override public void onAnimationEnd(Animator animator) {
                    Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_DAILY,
                        SelectionActivity.this, event.index,
                        event.parentIndex);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            })
            .setInterpolator(mInterpolator)
            .start();
    }


    @Subscribe
    public void handleResumeAnim(VideoDetailBackEvent event) {
        if (event.fromType != FromType.TYPE_DAILY) {
            return;
        }
        FrescoHelper.loadUrl(sdvAnim, event.url);

        if (event.hasScrolled) {
            if (event.theLast) {
                sdvAnim.animate()
                    .scaleX(1)
                    .setInterpolator(mInterpolator)
                    .scaleY(1)
                    .y(mEndY)
                    .setListener(mListener)
                    .setDuration(ANIMTION_DURATION)
                    .start();
            } else {
                sdvAnim.animate()
                    .scaleX(1)
                    .setInterpolator(mInterpolator)
                    .scaleY(1)
                    .y(mTitleHeight)
                    .setListener(mListener)
                    .setDuration(ANIMTION_DURATION)
                    .start();
            }
        } else {
            int[] l = new int[2];
            sdvAnim.getLocationInWindow(l);
            sdvAnim.animate()
                .scaleX(1)
                .scaleY(1)
                .y(lastY - l[1])
                .setListener(mListener)
                .setDuration(ANIMTION_DURATION)
                .setInterpolator(mInterpolator)
                .start();
        }

    }


    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
    }


    private int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            mStatusBarHeight = AppUtil.getStatusBarHeight(this);
        }
        return mStatusBarHeight;
    }

}
