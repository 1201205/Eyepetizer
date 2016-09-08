package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.VideoReply;
import com.hyc.eyepetizer.model.beans.ViewData;

import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public interface DailySelectionContract {
    interface View extends BaseView {
        void showSelection(List<ViewData> datas, boolean hasMore);
        void showError();
    }


    interface Presenter {
        void getDailySelection();
        void getMoreDailySelection();
    }
}
