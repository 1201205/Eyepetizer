package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.ViewData;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public interface VideoListContract {
    interface View extends BaseView {
        void showList(List<ViewData> datas);
        void showError();
        void noMore();
    }
}
