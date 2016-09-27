package com.hyc.eyepetizer.presenter;

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
public class LightTopicPresenter extends VideoListPresenter {
    private int mID;
    public LightTopicPresenter(VideoListContract.View view,int id) {
        super(view);
        mID=id;
    }

    @Override
    public void getVideoList() {
        mCompositeSubscription.add(
                Requests.getApi().getLightTopicsByID(mID).compose(new DefaultTransformer<Videos>()).subscribe(new Action1<Videos>() {
                    @Override
                    public void call(Videos videos) {
                        List<ViewData> datas=videos.getItemList();
                        datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                        mView.showList(datas);
                    }
                },new ExceptionAction(){
                    @Override
                    protected void onNoNetWork() {
                        super.onNoNetWork();
                    }
                })
        );
    }

    @Override
    public void getMore() {

    }
}
