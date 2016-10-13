package com.hyc.eyepetizer.presenter;

import android.net.Uri;
import android.text.TextUtils;
import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.PgcContract;
import com.hyc.eyepetizer.model.PgcModel;
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
                                        showPgc(videos, true);
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
                                        showPgc(videos, false);
                                    }
                                }, new ExceptionAction())
        );
    }


    private void showPgc(Videos videos, boolean add) {
        String nextUrl = videos.getNextPageUrl();
        List<ViewData> datas = videos.getItemList();
        if (add) {
            int count = datas.size();
            for (int i = 0; i < count; i++) {
                if (WidgetHelper.Type.VIDEO_COLLECTION_WITH_BRIEF.equals(datas.get(i).getType())) {
                    PgcModel.getInstance()
                        .putList(datas.get(i).getData().getHeader().getId(),
                            datas.get(i).getData().getItemList());
                }
            }
        }
        if (TextUtils.isEmpty(nextUrl)) {
            mView.noMore();
            datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
        } else {
            getNextParameter(nextUrl);
        }
        mView.showPgc(datas);
    }
}
