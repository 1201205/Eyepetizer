package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.Header;

/**
 * Created by ray on 16/9/16.
 */
public interface PgcContract {
    interface View extends BaseView {
        void showHead(Header header);
    }
}
