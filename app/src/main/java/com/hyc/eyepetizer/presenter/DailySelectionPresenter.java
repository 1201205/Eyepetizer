package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.DailySelectionContract;
import com.hyc.eyepetizer.model.DailySelectionModel;
import com.hyc.eyepetizer.model.beans.DailySelection;
import com.hyc.eyepetizer.model.beans.Issue;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.DataHelper;
import com.hyc.eyepetizer.utils.DateUtil;
import com.hyc.eyepetizer.utils.WidgetHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DailySelectionPresenter extends BasePresenter<DailySelectionContract.View> implements DailySelectionContract.Presenter {

    private boolean hasMore;
    private long nextDate;
    public DailySelectionPresenter(DailySelectionContract.View view) {
        super(view);
    }


    @Override public void detachView() {
        super.detachView();
        DailySelectionModel.getInstance().clear();
    }


    @Override
    public void getDailySelection() {
        mCompositeSubscription.add(
                Requests.getApi().getDailySelection(2).compose(new DefaultTransformer<DailySelection>()).map(mFunc1).subscribe(new Action1<List<ViewData>>() {
                    @Override
                    public void call(List<ViewData> datas) {
                        mView.showSelection(datas,false);
                    }
                },new ExceptionAction(){
                    @Override
                    protected void onNoNetWork() {
                        mView.showError();
                    }
                }));
    }

    @Override
    public void getMoreDailySelection() {
        mCompositeSubscription.add(
            Requests.getApi()
                .getDailySelection(2, nextDate)
                .compose(new DefaultTransformer<DailySelection>())
                .map(mFunc1)
                .subscribe(new Action1<List<ViewData>>() {
                    @Override
                    public void call(List<ViewData> datas) {
                        mView.showSelection(datas, false);
                    }
                }, new ExceptionAction()));
    }

    private Func1<DailySelection, List<ViewData>> mFunc1=new Func1<DailySelection, List<ViewData>>() {
        @Override
        public List<ViewData> call(DailySelection dailySelection) {
            hasMore = !TextUtils.isEmpty(dailySelection.getNextPageUrl());
            if (hasMore) {
                nextDate = DataHelper.getTime(dailySelection.getNextPageUrl());
            }
            if (dailySelection != null && dailySelection.getIssueList() != null) {
                List<ViewData> datas = new ArrayList<ViewData>();
                SparseArray<Integer> map = DailySelectionModel.getInstance().getMap();
                int c = map.size();
                int index = 0;
                if (c != 0) {
                    index = map.valueAt(c - 1) + 1;
                }
                for (Issue issue : dailySelection.getIssueList()) {
                    for (ViewData data : issue.getItemList()) {
                        if (WidgetHelper.Type.VIDEO.equals(data.getType())) {
                            DailySelectionModel.getInstance().addVideoData(data);
                            map.put(c, index);
                            data.setIndex(c);
                            c++;
                        }
                        index++;
                    }
                    DailySelectionModel.getInstance().addSection(index);
                    Date date=new Date(issue.getDate());
                    DailySelectionModel.getInstance().addDate(DateUtil.getMonthabbreviation(date).getShortName()+"."+DateUtil.getDay(date));
                    datas.addAll(issue.getItemList());
                }
                mView.setRefreshTime(dailySelection.getNextPublishTime());
                return datas;
            }

            return null;
        }
    };
}
