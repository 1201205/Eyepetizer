package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.contract.SelectionContract;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.SectionModel;
import com.hyc.eyepetizer.model.beans.SectionList;
import com.hyc.eyepetizer.model.beans.Selection;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.WidgetHelper;
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
    private FeedModel mModel;
    private Func1<Selection, List<ViewData>> mConvert = new Func1<Selection, List<ViewData>>() {
        @Override
        public List<ViewData> call(Selection selection) {
            mView.setNextPushTime(selection.getNextPublishTime());
            List<ViewData> datas = new ArrayList<ViewData>();
            int i = selection.getSectionList().size();
            for (int j = 0; j < i; j++) {
                SectionList bean = selection.getSectionList().get(j);
                if (bean.getHeader() != null) {
                    datas.add(bean.getHeader());
                }
                int count = bean.getItemList().size();
                int temp = 0;
                for (int c = 0; c < count; c++) {
                    ViewData data = bean.getItemList().get(c);
                    Log.e("datgetTypea",data.getType());
                    switch (data.getType()){
                        case WidgetHelper.Type.VIDEO:
                            bean.getItemList().get(c).setIndex(temp);
                            temp++;
                            break;
                        case  WidgetHelper.Type.VIDEO_COLLECTION_WITH_BRIEF:
                        case  WidgetHelper.Type.VIDEO_COLLECTION_WITH_TITLE:
                        case  WidgetHelper.Type.VIDEO_COLLECTION_WITH_COVER:
                            SectionModel.getInstance().putList(data.getData().getHeader().getId(),data.getData().getItemList());
                            break;
                    }
                    bean.getItemList().get(c).setParentIndex(bean.getId());
                }
                datas.addAll(bean.getItemList());
                if (bean.getFooter() != null) {
                    datas.add(bean.getFooter());
                }
                mModel.addSection(bean);
            }

            if (!TextUtils.isEmpty(selection.getNextPageUrl())) {
                mPageCount++;
            } else {
                mPageCount = NO_MORE;
                datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                mView.noMore();
            }
            return datas;
        }
    };


    public SelectionPresenter(SelectionContract.View view) {
        super(view);
        mModel = FeedModel.getInstance();
    }


    @Override
    public void getNextPage() {
        if (mPageCount != NO_MORE) {
            Requests.getApi()
                    .getMoreSelection(mPageCount, true)
                    .map(mConvert)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Action1<List<ViewData>>() {
                                @Override
                                public void call(List<ViewData> datas) {
                                    mView.showMoreSelection(datas);
                                }
                            });
        }
    }


    @Override
    public void getAndShowSelection() {
        mPageCount = 0;
        mModel.clear();
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
