package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.widget.CustomDraweeView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/9/2.
 */
public class VideoDetailActivity extends BaseActivity {

    @BindView(R.id.sdv_img)
    CustomDraweeView mImg;
    private static final String PARENT_INDEX = "parent_index";
    private static final String INDEX = "index";
    public static final String SHARE_PICTURE = "share_picture";

    public static Intent newIntent(Context context, int index, int parentIndex) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        return intent;
    }

    private int mParentIndex;
    private int mIndex;
    private List<ViewData> mViewDatas;

    @Override
    protected void handleIntent() {
        mParentIndex = getIntent().getIntExtra(PARENT_INDEX, -1);
        mIndex = getIntent().getIntExtra(INDEX, -1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setTransitionName(mImg, SHARE_PICTURE);
        mViewDatas = FeedModel.getInstance().getVideoListByIndex(mParentIndex);
        FrescoHelper.loadUrl(mImg, mViewDatas.get(mIndex).getData().getCover().getDetail());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_video_detail;
    }
}
