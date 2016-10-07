package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.RecommendsContract;
import com.hyc.eyepetizer.model.beans.Recommends;
import com.hyc.eyepetizer.net.Requests;
import rx.functions.Action1;

/**
 * Created by ray on 16/10/6.
 */

public class RecommendsPresenter extends BasePresenter<RecommendsContract.View>
    implements RecommendsContract.Presenter {
    public RecommendsPresenter(RecommendsContract.View view) {
        super(view);
    }


    @Override public void getRecommends() {
        mCompositeSubscription.add(
            Requests.getApi()
                .getRecommends()
                .compose(new DefaultTransformer<Recommends>())
                .subscribe(
                    new Action1<Recommends>() {
                        @Override public void call(Recommends recommends) {
                            mView.showRecommends(recommends.getItemList());
                        }
                    }, new ExceptionAction() {
                        @Override protected void onNoNetWork() {
                            mView.showError();
                        }
                    })
        );
    }
}
