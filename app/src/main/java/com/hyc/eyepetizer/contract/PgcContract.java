package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.Header;
import com.hyc.eyepetizer.model.beans.ViewData;

import java.util.List;

/**
 * Created by hyc on 16/9/16.
 */
public interface PgcContract {
    interface View extends BaseView {
        void showPgc(List<ViewData> datas);
        void showError();
        void noMore();
    }
    interface Presenter {
        void getPgcs();
        void getMorePgcs();
    }
}
