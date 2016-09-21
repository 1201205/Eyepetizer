package com.hyc.eyepetizer.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.hyc.eyepetizer.model.DailySelectionModel;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.VideoListInterface;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.ViewDataListFactory;
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
import org.greenrobot.eventbus.EventBus;
import rx.Subscription;

/**
 * Created by ray on 16/9/4.
 */
public class VideoDetailActivity2 extends BaseActivity {
    private static final String PARENT_INDEX = "parent_index";
    private static final String INDEX = "index";
    private static final String FORM_RELATE = "form_relate";
    private static final String VIDEO_ID = "video_id";
    private static final String TYPE = "type";
    private static final int STOP_ANIM = 1;
    private static final int START_ANIM = 2;
    private static final int POST_TO_PRE = 3;
    private static final int NOTIFY = 4;
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
    private Subscription mEventSubscription;
    private int mIndicatorScroll;
    private int mVideoID;
    private boolean fromRelate;
    private ValueAnimator mAnimator;
    private View mTarget;
    //最后一个selection
    private boolean fromTheLast;
    private int mFromType;
    private VideoListInterface mModel;
    private int mIndicatorWidth;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_ANIM:
                    mHandler.removeMessages(START_ANIM);
                    mAnimator.end();
                    resetAnimView(mTarget);
                    break;
                case START_ANIM:
                    mHandler.removeMessages(START_ANIM);
                    mTarget = mAdapter.getPrimaryItem().findViewById(R.id.sdv_img);
                    if (mTarget == null) {
                        mHandler.sendEmptyMessageDelayed(START_ANIM, 1000);
                        return;
                    }
                    mAnimator.start();
                    break;
                case POST_TO_PRE:
                    sendSelectMessage();
                    break;
                case NOTIFY:
                    mHandler.removeMessages(NOTIFY);
                    resetIndicator();
                    mAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };


    public static Intent newIntent(int type, Context context, int index, int parentIndex) {
        Intent intent = new Intent(context, VideoDetailActivity2.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        intent.putExtra(TYPE, type);
        return intent;
    }


    public static Intent newIntent(int type, Context context, int index, int parentIndex, int videoID) {
        Intent intent = newIntent(type, context, index, parentIndex);
        //intent.putExtra(FORM_RELATE, true);
        intent.putExtra(VIDEO_ID, videoID);
        return intent;
    }


    @Override
    protected void handleIntent() {
        Intent intent = getIntent();
        mFromType = intent.getIntExtra(TYPE, -1);
        mParentIndex = intent.getIntExtra(PARENT_INDEX, -1);
        mIndex = intent.getIntExtra(INDEX, -1);
        fromRelate = intent.getBooleanExtra(FORM_RELATE, false);
        //if (fromRelate) {
        mVideoID = intent.getIntExtra(VIDEO_ID, -1);
        //}
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initData();
        launchData(mIndex);
        initViewPager();
        mAnimator = ValueAnimator.ofFloat(1f, 1.2f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mTarget == null) {
                    mAnimator.cancel();
                    mHandler.sendEmptyMessageDelayed(START_ANIM, 1000);
                    return;
                }
                float f = valueAnimator.getAnimatedFraction() * 0.2f + 1;
                mTarget.setScaleX(f);
                mTarget.setScaleY(f);
            }
        });
        mAnimator.setDuration(5000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }


    private void initViewPager() {
        vpVideo.setOffscreenPageLimit(2);
        mAdapter = new VideoDetailAdapter(VideoDetailActivity2.this,
            mViewDatas);
        vpVideo.setAdapter(mAdapter);
        vpVideo.setCurrentItem(mIndex);
        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {
                    return;
                }
                if (preIndex > position) {
                    changeTextAlpha(positionOffset);
                    changeImageButtonAlpha(Math.abs(positionOffset - 0.5f) * 2);
                    llPart.setX(
                        ((positionOffset - 1) * mIndicatorScroll) + mIndicatorScroll * preIndex);
                } else if (preIndex <= position) {
                    changeTextAlpha(1 - positionOffset);
                    changeImageButtonAlpha(Math.abs(0.5f - positionOffset) * 2);
                    llPart.setX(positionOffset * mIndicatorScroll + mIndicatorScroll * preIndex);
                }
            }


            @Override
            public void onPageSelected(int position) {
                preIndex = position;
                launchData(position);
                changeTextAlpha(1);
                if (position != mIndex) {
                    hasScrolled = true;
                }
                //修复快速滑动引起的卡顿
                //如果马上执行会影响vp的速度 会有一点卡
                //修正位置：因为之前已经传入了开始位置，已经加入了本节非Video的item个数，所以现在应该减掉

            }


            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    mHandler.sendEmptyMessage(STOP_ANIM);
                    mHandler.removeMessages(POST_TO_PRE);
                } else if (state == 0) {
                    mHandler.sendEmptyMessageDelayed(START_ANIM, 1000);
                    if (!fromRelate) {
                        mHandler.sendEmptyMessageDelayed(POST_TO_PRE, 300);
                    }
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
                    mIndicatorWidth = llPart.getWidth();
                    resetIndicator();
                    mHandler.sendEmptyMessageDelayed(START_ANIM, 1000);
                    return true;
                }
            });

    }


    private void initData() {
        mIndexMap = new SparseArray<>();
        mModel = ViewDataListFactory.getModel(mFromType);
        mViewDatas = mModel.getVideoList(mVideoID, mParentIndex, mIndexMap);
        switch (mFromType) {
            case FromType.TYPE_MAIN:
                fromTheLast = FeedModel.getInstance().isTheLastSelection(mParentIndex);
                break;
            case FromType.TYPE_DAILY:
                fromTheLast = true;
                ((DailySelectionModel) mModel).setObserver(new DailySelectionModel.Observer() {
                    @Override
                    public void notifyDataSetAdd() {
                        //由于是一个一个的加入
                        mHandler.removeMessages(NOTIFY);
                        mHandler.sendEmptyMessageDelayed(NOTIFY, 50);
                    }
                });
                break;
            case FromType.TYPE_HISTORY:
            case FromType.TYPE_MONTH:
            case FromType.TYPE_WEEK:
            case FromType.TYPE_TAG_DATE:
            case FromType.TYPE_TAG_SHARE:
                fromTheLast = true;
                break;
            case FromType.TYPE_PGC_DATE:
            case FromType.TYPE_PGC_SHARE:
            case FromType.TYPE_CATEGORY_DATE:
            case FromType.TYPE_CATEGORY_SHARE:
                fromTheLast = true;
                ((VideoListModel) mModel).setObserver(new VideoListModel.Observer() {
                    @Override
                    public void notifyDataChanged() {
                        mHandler.sendEmptyMessage(NOTIFY);
                    }
                });
                break;

        }
        //        if (mFromType == FromType.TYPE_MAIN) {
        //            fromTheLast = FeedModel.getInstance().isTheLastSelection(mParentIndex);
        //        } else if (mFromType == FromType.TYPE_DAILY) {
        //            fromTheLast = true;
        //            ((DailySelectionModel)mModel).setObserver(new DailySelectionModel.Observer() {
        //                @Override
        //                public void notifyDataSetAdd() {
        //                    //由于是一个一个的加入
        //                    mHandler.removeMessages(NOTIFY);
        //                    mHandler.sendEmptyMessageDelayed(NOTIFY,50);
        //                }
        //            });
        //        }
    }


    private void resetIndicator() {
        mIndicatorScroll = (AppUtil.getScreenWidth(VideoDetailActivity2.this) - mIndicatorWidth) /
            (mViewDatas.size() - 1);
        llPart.setX(vpVideo.getCurrentItem() * mIndicatorScroll);
        tvPart.setText(
            String.format(AppUtil.getString(R.string.item_count), vpVideo.getCurrentItem() + 1,
                mViewDatas.size()));
    }


    private void stopAnimChar() {
        mTvTitle.stop();
        mTvCategory.stop();
        mTvDes.stop();
    }


    private void resetAnimView(View view) {
        if (view == null) {
            return;
        }
        view.setScaleX(1);
        view.setScaleY(1);
    }


    private void changeImageButtonAlpha(float alpha) {
        mRlButton.setAlpha(alpha);
    }


    @OnClick({ R.id.iv_back, R.id.iv_play, R.id.tv_reply_count, R.id.iv_more })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_play:
                VideoActivity.startList(mFromType, this, vpVideo.getCurrentItem(), mParentIndex,
                    true, mVideoID);
                break;
            case R.id.tv_reply_count:
                mRlButton.setVisibility(View.GONE);
                ItemListData data = mViewDatas.get(vpVideo.getCurrentItem()).getData();
                VideoReplyActivity.start(this, data.getId(), data.getConsumption().getReplyCount(),
                    data.getTitle(), data.getCover().getBlurred());
                break;
            case R.id.iv_more:
                ItemListData data2 = mViewDatas.get(vpVideo.getCurrentItem()).getData();
                VideoRelateActivity.start(this, data2.getId(), data2.getTitle(),
                    data2.getCover().getBlurred());
                break;
            default:
                break;
        }

    }


    private void animateText() {
        mTvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTitle.animateChar();
                mTvCategory.animateChar();
                mTvDes.animateChar();
            }
        }, 16);

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
            mRlAuthor.setVisibility(View.VISIBLE);
            FrescoHelper.loadUrl(mSdvIcon, author.getIcon());
            mTvAuthorName.setText(author.getName());
            mTvAuthorDes.setText(author.getDescription());
            mTvCount.setText(
                String.format(AppUtil.getString(R.string.video_count), author.getVideoNum()));
        } else {
            mRlAuthor.setVisibility(View.GONE);
        }
        tvPart.setText(
            String.format(AppUtil.getString(R.string.item_count), position + 1, mViewDatas.size()));
        mTvLikeCount.setText(String.valueOf(data.getConsumption().getCollectionCount()));
        mTvReplyCount.setText(String.valueOf(data.getConsumption().getReplyCount()));
        mTvShareCount.setText(String.valueOf(data.getConsumption().getShareCount()));
        animateText();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAnimator.isPaused()) {
            mAnimator.start();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRlButton.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onStop() {
        super.onStop();
        mAnimator.pause();
    }


    @Override
    public void onBackPressed() {
        Log.e("time--3", System.currentTimeMillis() + "");
        boolean theLast = false;
        stopAnimChar();
        if (fromTheLast) {
            theLast = vpVideo.getCurrentItem() == mViewDatas.size() - 1;
        }
        resetAnimView(mTarget);
        if (!fromRelate) {
            if (hasScrolled) {
                sendSelectMessage();
            }
            Log.e("hyc-po", vpVideo.getCurrentItem() + "--back--");
            EventBus.getDefault()
                .post(new VideoDetailBackEvent(mFromType, vpVideo.getCurrentItem(),
                    mViewDatas.get(vpVideo.getCurrentItem()).getData().getCover().getDetail(),
                    hasScrolled, theLast));
        }
        super.onBackPressed();
        Log.e("time--3", System.currentTimeMillis() + "");
    }


    @Override
    protected void onDestroy() {
        mAnimator.cancel();
        mAnimator.removeAllUpdateListeners();
        mTarget = null;
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        if (mEventSubscription != null) {
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


    private void sendSelectMessage() {
        //// TODO: 16/9/8   更换计算方式
        int index = 0;
        switch (mFromType) {
            case FromType.TYPE_DAILY:
            case FromType.TYPE_HISTORY:
            case FromType.TYPE_MONTH:
            case FromType.TYPE_WEEK:
            case FromType.TYPE_PGC_DATE:
            case FromType.TYPE_PGC_SHARE:
                index = vpVideo.getCurrentItem();
                break;
            case FromType.TYPE_MAIN:
                index = mIndexMap.get(vpVideo.getCurrentItem()) -
                    (mIndexMap.get(mIndex) - mIndex);
                break;

        }
        EventBus.getDefault().post(new VideoSelectEvent(mFromType, index));
    }
}
