package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.beans.Videos;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.WidgetHelper;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/9.
 */
public class CategoryPresenter extends VideoListPresenter {
    private String mTag;
    private VideoListModel mModel;
    private int mTypeID;
    private int mID;


    public CategoryPresenter(VideoListContract.View view) {
        super(view);
    }


    public CategoryPresenter(int typeID, int id, VideoListContract.View view, String tag) {
        super(view);
        mTag = tag;
        mTypeID = typeID;
        mID = id;
        mModel = VideoListModel.getInstance();
    }


    @Override
    public void getVideoList() {
        mCompositeSubscription.add(
                Requests.getApi()
                        .getCategoryByStrategy(mID, mTag)
                        .compose(new DefaultTransformer<Videos>())
                        .subscribe(new Action1<Videos>() {
                            @Override
                            public void call(Videos videos) {
                                mModel.addVideoList(mTypeID, videos.getItemList());
                                showList(videos);
                            }
                        }, new ExceptionAction() {
                            @Override
                            protected void onNoNetWork() {
                                mView.showError();
                            }
                        })
        );
    }

    @Override
    public void getMore() {
        mCompositeSubscription.add(
                Requests.getApi()
                        .getMoreCategoryByStrategy(mCount, mNum, mID, mTag)
                        .compose(new DefaultTransformer<Videos>())
                        .subscribe(new Action1<Videos>() {
                            @Override
                            public void call(Videos videos) {
                                mModel.addMore(mTypeID, videos.getItemList());
                              showList(videos);
                            }
                        }, new ExceptionAction())
        );
    }

    private void showList(Videos videos){

        List<ViewData> list = new ArrayList<ViewData>();
        list.addAll(videos.getItemList());
        if (TextUtils.isEmpty(videos.getNextPageUrl())) {
            list.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
            mView.noMore();
        } else {
            getNextParameter(videos.getNextPageUrl());
        }
        mView.showList(list);
    }

    @Override public void detachView() {
        super.detachView();
        mModel.clear(mTypeID);
    }
}
