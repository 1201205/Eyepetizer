package com.hyc.eyepetizer.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.PgcContract;
import com.hyc.eyepetizer.contract.RecommendsContract;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.beans.Recommends;
import com.hyc.eyepetizer.model.beans.Videos;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.WidgetHelper;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by hyc on 16/10/9.
 */

public class PgcsPresenter extends BasePresenter<PgcContract.View>
    implements PgcContract.Presenter {
    private int mCount;
    private int mNum;
    public PgcsPresenter(PgcContract.View view) {
        super(view);
    }


    @Override
    public void getPgcs() {
        mCompositeSubscription.add(
                Requests.getApi()
                        .getPgcs()
                        .compose(new DefaultTransformer<Videos>())
                        .subscribe(
                                new Action1<Videos>() {
                                    @Override public void call(Videos videos) {
                                        String nextUrl=videos.getNextPageUrl();
                                        List<ViewData> datas=videos.getItemList();
                                        if (TextUtils.isEmpty(nextUrl)) {
                                            mView.noMore();
                                            datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                                        } else {
                                            getNextParameter(nextUrl);
                                        }
                                        mView.showPgc(datas);
                                    }
                                }, new ExceptionAction() {
                                    @Override protected void onNoNetWork() {
                                        mView.showError();
                                    }
                                })
        );
    }

    @Override
    public void getMorePgcs() {
        mCompositeSubscription.add(
                Requests.getApi()
                        .getMorePgcs(mCount,mNum)
                        .compose(new DefaultTransformer<Videos>())
                        .subscribe(
                                new Action1<Videos>() {
                                    @Override public void call(Videos videos) {
                                        String nextUrl=videos.getNextPageUrl();
                                        List<ViewData> datas=videos.getItemList();
                                        if (TextUtils.isEmpty(nextUrl)) {
                                            mView.noMore();
                                            datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                                        } else {
                                            getNextParameter(nextUrl);
                                        }
                                        mView.showPgc(datas);
                                    }
                                }, new ExceptionAction())
        );
    }

    private void getNextParameter(String url){
        Uri uri=Uri.parse(url);
        mCount= Integer.valueOf(uri.getQueryParameter("start"));
        mNum= Integer.valueOf(uri.getQueryParameter("num"));
    }
}
