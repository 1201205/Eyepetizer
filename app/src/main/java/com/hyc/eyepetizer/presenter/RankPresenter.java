package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.beans.Videos;
import com.hyc.eyepetizer.net.Requests;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/9.
 */
public class RankPresenter extends VideoListPresenter {
    public RankPresenter(VideoListContract.View view) {
        super(view);
    }
    private String mTag;
    public RankPresenter(VideoListContract.View view,String tag) {
        super(view);
        mTag=tag;
    }
    @Override
    public void getVideoList() {
        mCompositeSubscription.add(
                Requests.getApi().getRankByStrategy(10,mTag).compose(new DefaultTransformer<Videos>()).subscribe(new Action1<Videos>() {
                    @Override
                    public void call(Videos videos) {
                        mView.showList(videos.getItemList());
                    }
                },new ExceptionAction(){
                    @Override
                    protected void onNoNetWork() {
                        mView.showError();
                    }
                })
        );
    }
}
