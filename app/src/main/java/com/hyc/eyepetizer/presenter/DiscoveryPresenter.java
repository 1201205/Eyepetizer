package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.DiscoveryContract;
import com.hyc.eyepetizer.model.beans.Videos;
import com.hyc.eyepetizer.net.Requests;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/22.
 */
public class DiscoveryPresenter extends BasePresenter<DiscoveryContract.View> implements DiscoveryContract.Presenter {
    public DiscoveryPresenter(DiscoveryContract.View view) {
        super(view);
    }

    @Override
    public void getDiscovery() {
        mCompositeSubscription.add(
                Requests.getApi().getDiscovery().compose(new DefaultTransformer<Videos>()).subscribe(new Action1<Videos>() {
                    @Override
                    public void call(Videos videos) {
                        mView.showDiscovery(videos.getItemList());
                    }
                },new ExceptionAction(){
                    @Override
                    protected void onNoNetWork() {
                        mView.onError();
                    }
                })
        );
    }
}
