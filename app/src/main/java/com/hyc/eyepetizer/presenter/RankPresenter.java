package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.beans.Videos;
import com.hyc.eyepetizer.net.Requests;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/9.
 */
public class RankPresenter extends VideoListPresenter {
    private String mTag;
    private VideoListModel mModel;
    private int mTypeID;
    public RankPresenter(VideoListContract.View view) {
        super(view);
    }


    public RankPresenter(int typeID, VideoListContract.View view, String tag) {
        super(view);
        mTag=tag;
        mTypeID = typeID;
        mModel = VideoListModel.getInstance();
    }
    @Override
    public void getVideoList() {
        mCompositeSubscription.add(
                Requests.getApi().getRankByStrategy(10,mTag).compose(new DefaultTransformer<Videos>()).subscribe(new Action1<Videos>() {
                    @Override
                    public void call(Videos videos) {
                        mModel.addVideoList(mTypeID, videos.getItemList());
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
