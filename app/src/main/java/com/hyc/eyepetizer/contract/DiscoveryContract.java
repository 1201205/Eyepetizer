package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.ViewData;

import java.util.List;

/**
 * Created by hyc on 2016/9/22.
 */
public interface DiscoveryContract {
    interface View extends BaseView{
        void showDiscovery(List<ViewData> viewDatas);
        void onError();
    }
    interface Presenter{
        void getDiscovery();
    }
}
