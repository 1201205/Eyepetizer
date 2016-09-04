package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import butterknife.BindView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.widget.AnimateTextView;
import com.hyc.eyepetizer.widget.CustomTextView;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class VideoDetailActivity extends BaseActivity {

    public static final String SHARE_PICTURE = "share_picture";
    private static final String PARENT_INDEX = "parent_index";
    private static final String INDEX = "index";
    @BindView(R.id.sdv_img)
    SimpleDraweeView mImg;
    @BindView(R.id.iv_back) ImageView ivBack;
    @BindView(R.id.iv_play) ImageView ivPlay;
    @BindView(R.id.tv_title) AnimateTextView tvTitle;
    @BindView(R.id.tv_category) AnimateTextView tvCategory;
    @BindView(R.id.iv_more) ImageView ivMore;
    @BindView(R.id.sdv_icon) SimpleDraweeView sdvIcon;
    @BindView(R.id.tv_author_name) CustomTextView tvAuthorName;
    @BindView(R.id.tv_count) CustomTextView tvCount;
    @BindView(R.id.tv_author_des) CustomTextView tvAuthorDes;
    @BindView(R.id.tv_des) AnimateTextView tvDes;
    @BindView(R.id.tv_like_count) CustomTextView tvLikeCount;
    @BindView(R.id.tv_share_count) CustomTextView tvShareCount;
    @BindView(R.id.tv_reply_count) CustomTextView tvReplyCount;
    @BindView(R.id.sdv_blur) SimpleDraweeView sdvBlur;
    private int mParentIndex;
    private int mIndex;
    private List<ViewData> mViewDatas;
    private ItemListData mdata;


    public static Intent newIntent(Context context, int index, int parentIndex) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        return intent;
    }


    @Override
    protected void handleIntent() {
        mParentIndex = getIntent().getIntExtra(PARENT_INDEX, -1);
        mIndex = getIntent().getIntExtra(INDEX, -1);

        Log.e("hyc-test123", "new---" + "---mIndex--" + mIndex + "-mParentIndex--" + mParentIndex);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSharedElementEnterTransition(
            DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
                ScalingUtils.ScaleType.CENTER_CROP));
        getWindow().setSharedElementReturnTransition(
            DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
                ScalingUtils.ScaleType.CENTER_CROP));
        mViewDatas = FeedModel.getInstance().getVideoListByIndex(mParentIndex);
        mdata = mViewDatas.get(mIndex).getData();
        FrescoHelper.loadUrl(mImg, mdata.getCover().getDetail());
        FrescoHelper.loadUrl(sdvBlur, mdata.getCover().getBlurred());
        tvTitle.setAnimText(mdata.getTitle());
        tvCategory.setAnimText(
            DataHelper.getCategoryAndDuration(mdata.getCategory(), mdata.getDuration()));
        tvDes.setAnimText(mdata.getDescription());
        tvDes.getViewTreeObserver().addOnPreDrawListener(
            new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    tvDes.getViewTreeObserver().removeOnPreDrawListener(this);
                    tvTitle.animateChar(100);
                    tvCategory.animateChar(100);
                    tvDes.animateChar(100);
                    return true;
                }
            });
        //ViewCompat.setTransitionName(mImg, SHARE_PICTURE);

    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }
}
