package com.hyc.eyepetizer.view;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.event.StartVideoDetailEvent;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.LightTopicPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.TestAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.MyAnimatorListener;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LightTopicActivity extends AnimateActivity<LightTopicPresenter> implements VideoListContract.View{
    @BindView(R.id.rv_video)
    RecyclerView mRvVideo;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error)
    CustomTextView mTvError;
    @BindView(R.id.rl_error)
    RelativeLayout mRlError;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvHeadTitle;
    private TestAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int mLastIndex;
    private int mID;
    private String mTitle;


    public static Intent getIntent(Context context, String title, int id) {
        Intent intent = new Intent(context, LightTopicActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        return intent;
    }


    @Override
    protected void handleIntent() {
        Intent intent = getIntent();
        mID = intent.getIntExtra("id", -1);
        mTitle = intent.getStringExtra("title");
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_light_topics;
    }


    @Override
    public void showList(List<ViewData> datas) {
        if (mAdapter == null) {
            if (mRvVideo.getVisibility()== View.GONE) {
                mRvVideo.setVisibility(View.VISIBLE);
                mRlError.setVisibility(View.GONE);
            }
            mAdapter = new TestAdapter.Builder(this, datas).type(FromType.TYPE_LIGHT_TOPIC).build();
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
    }


    @Override
    protected boolean canDeal(int type) {
        return type == FromType.TYPE_LIGHT_TOPIC;
    }

    @Override
    protected void onStartAnimEnd(StartVideoDetailEvent event) {
        Intent intent = VideoDetailActivity2.newIntent(FromType.TYPE_LIGHT_TOPIC,
                LightTopicActivity.this, event.position,
                event.parentIndex, mID);
        mLastIndex=event.position;
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStartResumeAnim(VideoDetailBackEvent event) {
        if (event.position!=mLastIndex) {
            FrescoHelper.loadUrl(sdvAnim, event.url);
        }
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
        mEndY = (int) (AppUtil.getScreenHeight(this) - getStatusBarHeight() -
                mItemHeight - getResources().getDimensionPixelSize(R.dimen.no_more_height));
    }

    @Override
    protected void initView() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvHeadTitle.setText(mTitle);
        tvHeadTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        mManager=new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvVideo.setLayoutManager(mManager);
    }

    @Override
    protected void initPresenterAndData() {
        mPresenter=new LightTopicPresenter(this,mID);
        mPresenter.getVideoList();
    }

    @Subscribe
    public void handleSelectEvent(VideoSelectEvent event) {
        if (event.fromType != FromType.TYPE_LIGHT_TOPIC || mLastIndex == event.position) {
            return;
        }
        FrescoHelper.loadUrl(sdvAnim,event.url);
        mLastIndex=event.position;
        mManager.scrollToPosition(mLastIndex);
        mRvVideo.postDelayed(new Runnable() {
            @Override
            public void run() {
                View v = mRvVideo.getChildAt(mLastIndex - mManager.findFirstVisibleItemPosition());
                final int[] l=new int[2];
                v.getLocationInWindow(l);
                mRvVideo.scrollBy(0, (int) (l[1]-mTitleHeight-getStatusBarHeight()));
            }
        }, 5);
    }
}
