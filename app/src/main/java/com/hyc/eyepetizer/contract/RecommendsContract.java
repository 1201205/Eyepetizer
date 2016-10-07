package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/10/4.
 */

public interface RecommendsContract {
    interface View extends BaseView {
        void showRecommends(List<ViewData> dataList);
        void showError();
    }


    interface Presenter {
        void getRecommends();
    }
}
