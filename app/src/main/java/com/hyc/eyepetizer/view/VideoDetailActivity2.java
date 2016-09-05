package com.hyc.eyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.event.VideoSelectEvent;
import com.hyc.eyepetizer.event.VideoSelectEvent2;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.view.adapter.VideoDetailAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by ray on 16/9/4.
 */
public class VideoDetailActivity2 extends BaseActivity {
    private static final String PARENT_INDEX = "parent_index";
    private static final String INDEX = "index";
    @BindView(R.id.sdv_img) SimpleDraweeView sdvImg;
    @BindView(R.id.vp_video) ViewPager vpVideo;
    @BindView(R.id.tv_part) CustomTextView tvPart;
    @BindView(R.id.ll_part) LinearLayout llPart;
    private int mParentIndex;
    private int mIndex;
    private List<ViewData> mViewDatas;
    private boolean hasScrolled;

    public static Intent newIntent(Context context, int index, int parentIndex) {
        Intent intent = new Intent(context, VideoDetailActivity2.class);
        intent.putExtra(INDEX, index);
        intent.putExtra(PARENT_INDEX, parentIndex);
        return intent;
    }


    @Override protected void handleIntent() {
        mParentIndex = getIntent().getIntExtra(PARENT_INDEX, -1);
        mIndex = getIntent().getIntExtra(INDEX, -1);
    }


    @Override protected int getLayoutID() {
        return R.layout.activity_video_d;
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setShareElementTransition();
        vpVideo.setOffscreenPageLimit(2);
        mViewDatas = FeedModel.getInstance().getVideoListByIndex(mParentIndex);
        vpVideo.setAdapter(new VideoDetailAdapter(VideoDetailActivity2.this,
                mViewDatas));
        vpVideo.setCurrentItem(mIndex);
        ItemListData data = mViewDatas.get(mIndex).getData();
        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position!=mIndex) {
                    hasScrolled=true;
                }
                EventBus.getDefault().post(new VideoSelectEvent2(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        FrescoHelper.loadUrl(sdvImg, data.getCover().getDetail());
//        setEnterSharedElementCallback(new SharedElementCallback() {
//            @Override
//            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//
//                sdvImg.postDelayed(new Runnable() {
//                    @Override public void run() {
//                        sdvImg.setVisibility(View.INVISIBLE);
//                    }
//                }, 400)
//                ;
//            }
//        });

    }


    @Override protected void onResume() {
        super.onResume();
    }


    @Override public void onBackPressed() {
        EventBus.getDefault().post(new VideoSelectEvent(vpVideo.getCurrentItem(),mViewDatas.get(vpVideo.getCurrentItem()).getData().getCover().getDetail(),hasScrolled));
        super.onBackPressed();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
