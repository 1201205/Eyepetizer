package com.hyc.eyepetizer.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import com.asha.md360player4android.MD360PlayerActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseActivity;
import com.hyc.eyepetizer.contract.RecommendsContract;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.beans.ItemListData;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.RecommendsPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.FrescoHelper;
import com.hyc.eyepetizer.utils.TypefaceHelper;
import com.hyc.eyepetizer.view.adapter.RecommendsAdapter;
import com.hyc.eyepetizer.widget.CustomTextView;
import com.hyc.eyepetizer.widget.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 16/10/6.
 */

public class RecommendsActivity extends BaseActivity<RecommendsPresenter> implements
    RecommendsContract.View, SwipeFlingAdapterView.onFlingListener,
    SwipeFlingAdapterView.OnItemClickListener {
    @BindView(R.id.sdv_blur) SimpleDraweeView sdvBlur;
    @BindView(R.id.tv_indicator) CustomTextView tvIndicator;
    @BindView(R.id.sf_recommends) SwipeFlingAdapterView sfRecommends;
    @BindView(R.id.sdv_img) SimpleDraweeView sdvImg;
    @BindView(R.id.tv_title) CustomTextView tvTitle;
    @BindView(R.id.tv_category) CustomTextView tvCategory;
    @BindView(R.id.rl_flow) RelativeLayout rlFlow;
    @BindView(R.id.sdv_below_blur) SimpleDraweeView sdvBelowBlur;
    @BindView(R.id.tv_des) CustomTextView tvDes;
    @BindView(R.id.ll_hint) LinearLayout llHint;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_head_title)
    CustomTextView tvHeadTitle;

    private RecommendsAdapter mAdapter;
    private List<ViewData> mRemoved;
    private int mCount;


    @Override protected void handleIntent() {

    }


    @Override protected int getLayoutID() {
        return R.layout.activity_recommends;
    }


    @Override public void showRecommends(List<ViewData> dataList) {
        mRemoved = new ArrayList<>();
        mAdapter.addAll(dataList);
        setHintView(dataList.get(0).getData());
        FrescoHelper.loadUrl(sdvBlur, mAdapter.getItem(0).getData().getCover().getBlurred());
        mCount=dataList.size();
        tvIndicator.setText(String.format(AppUtil.getString(R.string.item_count),1,mCount));
    }


    @Override public void showError() {
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
        tvHeadTitle.setText(R.string.recommend);
        tvHeadTitle.setTypeface(TypefaceHelper.getTypeface(TypefaceHelper.BOLD));
        sfRecommends.setFlingListener(this);
        sfRecommends.setOnItemClickListener(this);
        sfRecommends.getViewTreeObserver()
            .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sfRecommends.getViewTreeObserver().removeOnPreDrawListener(this);
                    llHint.setX(-llHint.getWidth());
                    sfRecommends.setAddView(llHint, tvIndicator.getHeight());
                    //FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) llHint.getLayoutParams();
                    //Log.e("hyc-p",params.height+"---"+params.width+"----"+params.topMargin+"----"+params.bottomMargin+"----"+params.leftMargin+"----"+params.rightMargin+"---")    ;

                    return true;
                }
            });
        mAdapter = new RecommendsAdapter(this);
        sfRecommends.setAdapter(mAdapter);
    }

    @Override
    protected void initPresenterAndData() {
        mPresenter = new RecommendsPresenter(this);
        mPresenter.attachView();
        mPresenter.getRecommends();
    }


    @Override public void onItemClicked(MotionEvent event, View v, Object dataObject) {
        ViewData data= (ViewData) dataObject;
        ItemListData itemListData = data.getData();
        if ("PANORAMIC".equals(itemListData.getType())) {
            MD360PlayerActivity.startVideo(this, itemListData.getPlayUrl());
        } else {
            VideoActivity.startList(FromType.TYPE_RECOMMENDS, this, mRemoved.size(), 0,
                    false, FromType.TYPE_RECOMMENDS);
        }
    }


    @Override public void removeFirstObjectInAdapter() {
        mAdapter.remove(0);
        sfRecommends.setRemoveInLayout();
        sfRecommends.removeFirst();
        tvIndicator.setText(String.format(AppUtil.getString(R.string.item_count),mRemoved.size()+1,mCount));
        FrescoHelper.loadUrl(sdvBlur, mAdapter.getItem(0).getData().getCover().getBlurred());
    }


    @Override public void onLeftCardExit(Object dataObject) {
        ViewData data = (ViewData) dataObject;
        mRemoved.add(data);
        llHint.setVisibility(View.INVISIBLE);
        setHintView(data.getData());
    }


    private void setHintView(ItemListData data) {
        FrescoHelper.loadUrl(sdvImg, data.getCover().getDetail());
        FrescoHelper.loadUrl(sdvBelowBlur, data.getCover().getBlurred());
        tvDes.setText(data.getDescription());
        tvTitle.setText(data.getTitle());
        tvCategory.setText(
            DataHelper.getCategoryAndDuration(data.getCategory(), data.getDuration()));
    }


    @Override public void onRightCardExit(Object dataObject) {

    }


    @Override public void onAdapterAboutToEmpty(int itemsInAdapter) {

    }


    @Override public void onScroll(float progress, float scrollXProgress) {

    }


    @Override public void onReAdd() {
        if (mRemoved.size() == 0) {
            return;
        }
        mAdapter.reAdd(mRemoved.get(mRemoved.size() - 1));
        sfRecommends.addFirst();
        mRemoved.remove(mRemoved.size() - 1);

//        sfRecommends.requestLayout();
        FrescoHelper.loadUrl(sdvBlur, mAdapter.getItem(0).getData().getCover().getBlurred());
        llHint.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRemoved.size() > 0) {
                    setHintView(mRemoved.get(mRemoved.size() - 1).getData());
                }
                llHint.setVisibility(View.INVISIBLE);
            }
        },100);
        tvIndicator.setText(String.format(AppUtil.getString(R.string.item_count),mRemoved.size()+1,mCount));
    }

}
