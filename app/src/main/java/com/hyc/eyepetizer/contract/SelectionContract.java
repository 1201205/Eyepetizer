package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/8/30.
 */
public interface SelectionContract {
    interface View extends BaseView {
        void showSelection(List<ViewData> datas);
        void showMoreSelection(List<ViewData> datas);
        void noMore();
        void onRefresh();
        void setNextPushTime(long time);
    }


    interface Presenter {
        void getAndShowSelection();
        void getNextPage();
    }
}
