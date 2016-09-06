package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public interface VideoRelateContract {
    interface View extends BaseView {
        void showView(List<ViewData> datas);
    }


    interface Presenter {
        void getRelate(int id);
    }
}
