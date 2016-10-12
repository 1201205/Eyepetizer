package com.hyc.eyepetizer.base;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by hyc on 16/4/19.
 */
public class BasePresenter<T extends BaseView> {
    public CompositeSubscription mCompositeSubscription;
    protected T mView;


    public BasePresenter(T view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }


    public void attachView() {

    }


    public void detachView() {
        if (mCompositeSubscription!=null) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
        mView = null;
    }
}
