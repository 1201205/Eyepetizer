package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.event.VideoDetailBackEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.beans.Author;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.VideoDetailAdapter;
import com.hyc.eyepetizer.widget.AnimateTextView;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.DepthPageTransformer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ray on 16/9/4.
 */
public class VideoDetailActivity2 extends BaseActivity {
    private static final String PARENT_INDEX = "parent_index";
    private static final String INDEX = "index";
    private static final String FORM_RELATE = "form_relate";
    private static final String VIDEO_ID = "video_id";
    @BindView(R.id.vp_video)
    ViewPager vpVideo;
    @BindView(R.id.tv_part)
    CustomTextView tvPart;
    @BindView(R.id.ll_part)
    LinearLayout llPart;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.tv_title)
    AnimateTextView mTvTitle;
    @BindView(R.id.tv_category)
    AnimateTextView mTvCategory;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.sdv_icon)
    SimpleDraweeView mSdvIcon;
    @BindView(R.id.tv_author_name)
    CustomTextView mTvAuthorName;
    @BindView(R.id.tv_count)
    CustomTextView mTvCount;
    @BindView(R.id.tv_author_des)
    CustomTextView mTvAuthorDes;
    @BindView(R.id.rl_author)
    RelativeLayout mRlAuthor;
    @BindView(R.id.divider)
    View mDivider;
    @BindView(R.id.tv_des)
    AnimateTextView mTvDes;
    @BindView(R.id.tv_like_count)
    CustomTextView mTvLikeCount;
    @BindView(R.id.tv_share_count)
    CustomTextView mTvShareCount;
    @BindView(R.id.tv_reply_count)
    CustomTextView mTvReplyCount;
    @BindView(R.id.ll_floor)
    LinearLayout mLlFloor;
    @BindView(R.id.rl_text)
    RelativeLayout mRlText;
    @BindView(R.id.rl_button)
    RelativeLayout mRlButton;
    private int mParentIndex;
    private int mIndex;
    private List<ViewData> mViewDatas;
    private boolean hasScrolled;
    private VideoDetailAdapter mAdapter;
    private int preIndex;
    private SparseArray<Integer> mIndexMap;
    private boolean dragging;
    private Subscription mEventSubscription;
    private int mIndicatorScroll;
    private int mVideoID;
    private boolean fromRelate;
    private Runnable mEvent = new Runnable() {
        @Override
        public void run() {
            EventBus.getDefault()
                .post(new VideoSelectEvent(
                    mIndexMap.get(vpVideo.getCurrentItem()) - (mIndexMap.get(mIndex) - mIndex)));
        }
    };
    private int lastValue;


    public static Intent newIntent(Context context, int index, int parentIndex) {
        Intent intent = new Intent(context, VideoDetailActivity2.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        return intent;
    }


    public static Intent newIntent(Context context, int index, int parentIndex, boolean formRelate, int videoID) {
        Intent intent = newIntent(context, index, parentIndex);
        intent.putExtra(FORM_RELATE, formRelate);
        intent.putExtra(VIDEO_ID, videoID);
        return intent;
    }


    @Override
    protected void handleIntent() {
        mParentIndex = getIntent().getIntExtra(PARENT_INDEX, -1);
        mIndex = getIntent().getIntExtra(INDEX, -1);
        fromRelate = getIntent().getBooleanExtra(FORM_RELATE, false);
        mVideoID = getIntent().getIntExtra(VIDEO_ID, -1);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mIndexMap=new SparseArray<>();
        vpVideo.setOffscreenPageLimit(2);
        if (fromRelate) {
            mViewDatas = FeedModel.getInstance().getRelate(mVideoID, mParentIndex);
        } else {
            mViewDatas = FeedModel.getInstance().getVideoListByIndex(mParentIndex, mIndexMap);
        }
        launchData(mIndex);
        mAdapter = new VideoDetailAdapter(VideoDetailActivity2.this,
                mViewDatas);
        vpVideo.setAdapter(mAdapter);
        vpVideo.setCurrentItem(mIndex);
        ItemListData data = mViewDatas.get(mIndex).getData();
        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset==0) {
                    return;
                }
                    if (preIndex > position) {
                        changeTextAlpha(positionOffset);
                        changeImageButtonAlpha(Math.abs(positionOffset - 0.5f) * 2);
                        llPart.setX(((positionOffset - 1) * mIndicatorScroll) + mIndicatorScroll * preIndex);
                    } else if (preIndex <= position) {
                        changeTextAlpha(1 - positionOffset);
                        changeImageButtonAlpha(Math.abs(0.5f - positionOffset) * 2);
                        llPart.setX(positionOffset * mIndicatorScroll + mIndicatorScroll * preIndex);
                    }

                lastValue=positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                dragging=false;
                preIndex = position;
                launchData(position);
                changeTextAlpha(1);
                if (position != mIndex) {
                    hasScrolled = true;
                }
                //修复快速滑动引起的卡顿
                //如果马上执行会影响vp的速度 会有一点卡
                //修正位置：因为之前已经传入了开始位置，已经加入了本节非Video的item个数，所以现在应该减掉
                if (!fromRelate) {
                    if (mEventSubscription != null) {
                        mEventSubscription.unsubscribe();
                    }
                    postEvent(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    dragging = true;
                } else {
                    dragging=false;
                }
            }
        });
        vpVideo.setPageTransformer(true, new DepthPageTransformer());
        vpVideo.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        vpVideo.getViewTreeObserver().removeOnPreDrawListener(this);
                        animateText();
                        int w=llPart.getWidth();
                        mIndicatorScroll= (AppUtil.getScreenWidth(VideoDetailActivity2.this)-w)/(mViewDatas.size()-1);
                        llPart.setX(mIndex*mIndicatorScroll);
                        return true;
                    }
                });
    }

    private void stopAnimChar() {
        mTvTitle.stop();
        mTvCategory.stop();
        mTvDes.stop();
    }

    private void postEvent(int p){
        mEventSubscription=Observable.just(new VideoSelectEvent(mIndexMap.get(vpVideo.getCurrentItem())-(mIndexMap.get(mIndex)-mIndex))).delay(500,TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<VideoSelectEvent>() {
            @Override
            public void call(VideoSelectEvent event) {
                EventBus.getDefault().post(event);
            }
        });
    }

    private void changeImageButtonAlpha(float alpha) {
        mRlButton.setAlpha(alpha);
    }

    @OnClick(R.id.iv_back)
    public void back(){
        onBackPressed();
    }
    @OnClick(R.id.iv_play)
    public void play(){
        VideoActivity.startList(this,vpVideo.getCurrentItem(),mParentIndex);
    }


    @OnClick(R.id.iv_more)
    public void goToRelate() {
        ItemListData data = mViewDatas.get(vpVideo.getCurrentItem()).getData();
        VideoRelateActivity.start(this, data.getId(), data.getTitle(),
            data.getCover().getBlurred());
    }
    private void animateText() {
        mTvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTitle.animateChar();
                mTvCategory.animateChar();
                mTvDes.animateChar();
            }
        },16);

    }

    private void changeTextAlpha(float alpha) {
        mRlText.setAlpha(alpha);
    }

    private void launchData(int position) {
        ItemListData data = mViewDatas.get(position).getData();
        mTvTitle.setAnimText(data.getTitle());
        mTvCategory.setAnimText(
                DataHelper.getCategoryAndDuration(data.getCategory(), data.getDuration()));
        mTvDes.setAnimText(data.getDescription());
        Author author = data.getAuthor();
        if (author != null) {
            FrescoHelper.loadUrl(mSdvIcon, author.getIcon());
            mTvAuthorName.setText(author.getName());
            mTvAuthorDes.setText(author.getDescription());
            mTvCount.setText(author.getVideoNum() + "个视频");
        } else {
            mRlAuthor.setVisibility(View.GONE);
        }
        tvPart.setText((position+1)+" - "+mViewDatas.size());
        mTvLikeCount.setText(String.valueOf(data.getConsumption().getCollectionCount()));
        mTvReplyCount.setText(String.valueOf(data.getConsumption().getReplyCount()));
        mTvShareCount.setText(String.valueOf(data.getConsumption().getShareCount()));
        animateText();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        if (!fromRelate) {
            if (hasScrolled) {
                EventBus.getDefault()
                    .post(new VideoSelectEvent(mIndexMap.get(vpVideo.getCurrentItem()) -
                        (mIndexMap.get(mIndex) - mIndex)));
            }
            EventBus.getDefault()
                .post(new VideoDetailBackEvent(vpVideo.getCurrentItem(),
                    mViewDatas.get(vpVideo.getCurrentItem()).getData().getCover().getDetail(),
                    hasScrolled));
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventSubscription!=null) {
            mEventSubscription.unsubscribe();
        }

    }


    @Override
    public void finish() {
        super.finish();
        if (!fromRelate) {
            overridePendingTransition(0, 0);
        }
    }
}
