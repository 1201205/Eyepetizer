package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;

import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.beans.Videos;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.WidgetHelper;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/27.
 */
public class SpecialTopicsPresenter extends VideoListPresenter {

    public SpecialTopicsPresenter(VideoListContract.View view) {
        super(view);
    }

    @Override
    public void getVideoList() {
        mCompositeSubscription.add(
                Requests.getApi().getSpecialTopics().compose(new DefaultTransformer<Videos>()).subscribe(new Action1<Videos>() {
                    @Override
                    public void call(Videos videos) {
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
                Requests.getApi().getMoreSpecialTopics(mCount, mNum).compose(new DefaultTransformer<Videos>()).subscribe(new Action1<Videos>() {
                    @Override
                    public void call(Videos videos) {
                        showList(videos);
                    }
                })
        );
    }

    private void showList(Videos videos) {
        List<ViewData> datas = videos.getItemList();
        if (TextUtils.isEmpty(videos.getNextPageUrl())) {
            mView.noMore();
            datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
        } else {
            getNextParameter(videos.getNextPageUrl());
        }
        mView.showList(videos.getItemList());
    }
}
