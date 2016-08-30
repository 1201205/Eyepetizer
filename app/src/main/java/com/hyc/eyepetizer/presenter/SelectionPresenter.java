package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;
import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.beans.SectionList;
import com.hyc.eyepetizer.beans.Selection;
import com.hyc.eyepetizer.beans.ViewData;
import com.hyc.eyepetizer.contract.SelectionContract;
import com.hyc.eyepetizer.net.Requests;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ray on 16/8/30.
 */
public class SelectionPresenter extends BasePresenter<SelectionContract.View>
    implements SelectionContract.Presenter {
    private static final int NO_MORE = -1;
    private int mPageCount;
    private Func1<Selection, List<ViewData>> mConvert = new Func1<Selection, List<ViewData>>() {
        @Override public List<ViewData> call(Selection selection) {
            List<ViewData> datas = new ArrayList<ViewData>();
            int i = selection.getSectionList().size();
            for (int j = 0; j < i; j++) {
                SectionList bean = selection.getSectionList().get(j);
                if (bean.getHeader() != null) {
                    datas.add(bean.getHeader());
                }
                datas.addAll(bean.getItemList());
                if (bean.getFooter() != null) {
                    datas.add(bean.getFooter());
                }
            }
            if (!TextUtils.isEmpty(selection.getNextPageUrl())) {
                mPageCount++;
            } else {
                mPageCount = NO_MORE;
            }
            return datas;
        }
    };


    public SelectionPresenter(SelectionContract.View view) {
        super(view);
    }


    @Override public void getNextPage() {
        if (mPageCount != NO_MORE) {
            Requests.getApi()
                .getMoreSelection(mPageCount, true)
                .map(mConvert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    new Action1<List<ViewData>>() {
                        @Override public void call(List<ViewData> datas) {
                            mView.showMoreSelection(datas);
                        }
                    });
        }
    }


    @Override public void getAndShowSelection() {
        Requests.getApi()
            .getSelection()
            .subscribeOn(Schedulers.io())
            .map(mConvert)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<ViewData>>() {
                @Override
                public void call(List<ViewData> selections) {
                    mView.showSelection(selections);
                }
            });
    }
}
