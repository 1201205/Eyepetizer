package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.ViewData;

import java.util.List;

/**
 * Created by hyc on 2016/9/27.
 */
public interface SpecialTopicsContract {
    interface View extends BaseView{
        void showTopics(List<ViewData> datas);
        void showError();
    }
}
