package com.hyc.eyepetizer.base;

import android.net.Uri;
import android.util.Log;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by hyc on 16/4/19.
 */
public class BasePresenter<T extends BaseView> {
    public CompositeSubscription mCompositeSubscription;
    protected T mView;
    protected int mCount;
    protected int mNum;

    public BasePresenter(T view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }


    public void attachView() {

    }


    public void detachView() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
        mView = null;
    }

    protected void getNextParameter(String url) {
        Uri uri = Uri.parse(url);
        mCount = Integer.valueOf(uri.getQueryParameter("start"));
        mNum = Integer.valueOf(uri.getQueryParameter("num"));
        Log.e("hyc-p",mCount+"-----"+mNum);
    }
}
