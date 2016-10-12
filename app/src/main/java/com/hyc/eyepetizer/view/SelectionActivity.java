package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.LoadingAnimView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import com.hyc.eyepetizer.widget.PullToRefreshView;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * todo 还有一个界面也是类似的 重复代码过多 需要进行重构
 * Created by Administrator on 2016/9/8.
 */
public class SelectionActivity extends AnimateActivity<DailySelectionPresenter> implements
        DailySelectionContract.View {
    private static final long ANIMATION_DURATION = 350;
    @BindView(R.id.tv_remain)
    CustomTextView tvRemain;
    @BindView(R.id.loading)
    LoadingAnimView loading;
    @BindView(R.id.head)
    LinearLayout head;
    @BindView(R.id.target)
    RecyclerView target;
    @BindView(R.id.ptrf_main)
    PullToRefreshView ptrfMain;
    @BindView(R.id.sdv_anim)
    SimpleDraweeView sdvAnim;
    @BindView(R.id.fl_all)
    FrameLayout flAll;
    @BindView(R.id.tv_right)
    CustomTextView tvRight;
    @BindView(R.id.img_left)
    ImageView ivLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvTitle;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
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
        @Override
        public void onAnimationEnd(Animator animator) {
            sdvAnim.setVisibility(View.GONE);
        }
    };
    private int mLastIndex;
    private SparseArray<Integer> mMap;
    private LinearLayoutManager mManager;
    private int mStatusBarHeight;
    private int mDatePosition;
    private boolean isStarting;

    @Override
    protected void handleIntent() {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_daily_selection;
    }

    @Override
    public void showSelection(List<ViewData> datas, boolean hasMore) {
        if (mAdapter == null) {
            ptrfMain.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
            mMap = DailySelectionModel.getInstance().getMap();
            TestAdapter.Builder builder = new TestAdapter.Builder(this);
            mAdapter = builder.type(FromType.TYPE_DAILY).build();
            target.setAdapter(mAdapter);
        }
        isRequesting = false;
        mAdapter.addData(datas);
    }

    @Override
    public void setRefreshTime(long time) {
        ptrfMain.setNextPushTime(time);
    }

    @Subscribe
    public void handleSelectEvent(VideoSelectEvent event) {
        final int p = mMap.get(event.position);
        if (event.fromType != FromType.TYPE_DAILY || mManager.findFirstCompletelyVisibleItemPosition() == p) {
            return;
        }
        if (mLastIndex != event.position) {
            FrescoHelper.loadUrl(sdvAnim, DailySelectionModel.getInstance().getVideoList(0, 0, null).get(event.position).getData().getCover().getDetail());
        }
        mLastIndex = event.position;
        mManager.scrollToPosition(p);
        target.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v = target.getChildAt(p - mManager.findFirstVisibleItemPosition());
                final int[] l = new int[2];
                v.getLocationInWindow(l);
                target.scrollBy(0, (int) (l[1] - mTitleHeight - getStatusBarHeight()));
            }
        }, 5);
    }

    @Override
    public void showError() {
        ptrfMain.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean canDeal(int type) {
        return !(type != FromType.TYPE_DAILY || isStarting || loading.isLoading());
    }

    @Override
    protected void onStartAnimEnd(StartVideoDetailEvent event) {
        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_DAILY,
                SelectionActivity.this, event.index,
                event.parentIndex);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStartResumeAnim(VideoDetailBackEvent event) {
        if (mLastIndex != event.position) {
            FrescoHelper.loadUrl(sdvAnim, event.url);
        }
        mLastIndex = event.position;
    }

    @Override
    protected boolean hasIndicator() {
        return false;
    }

    @Override
    protected int getStartY(int y) {
        return y - getStatusBarHeight();
    }

    @Override
    protected void initEndY() {
        mEndY = (int) (AppUtil.getScreenHeight(this) - getStatusBarHeight() - mItemHeight);
    }

    @OnClick({R.id.img_left,R.id.rl_error})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_left:
                finish();
                break;
            case R.id.rl_error:
                mPresenter.getDailySelection();
                break;
        }
    }
    @Override
    protected void initView() {
        tvTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        tvTitle.setText(R.string.selection);
        ivLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.today);
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
                setDate();
            }
        };
        target.setLayoutManager(mManager);
        target.addOnScrollListener(mOnScrollListener);
    }

    private void setDate() {
        SparseArray<Integer> s = DailySelectionModel.getInstance().getSection();
        int size = s.size();
        int position = mManager.findFirstVisibleItemPosition();
        if (position <= s.get(mDatePosition)) {
            if (mDatePosition == 0 || position > s.get(mDatePosition - 1)) {
                return;
            }
        }
        if (position > s.get(mDatePosition)) {
            mDatePosition++;
        } else if (position <= s.get(mDatePosition - 1)) {
            mDatePosition--;
        }
        tvRight.setText(DailySelectionModel.getInstance().getDates().get(mDatePosition));
    }
    @Override
    protected void initPresenterAndData() {
        mPresenter = new DailySelectionPresenter(this);
        mPresenter.attachView();
        mPresenter.getDailySelection();

    }


}
