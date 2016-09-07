package com.hyc.eyepetizer.contract;

import com.hyc.eyepetizer.base.BaseView;
import com.hyc.eyepetizer.model.beans.VideoReply;

import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public interface VideoReplyContract {
    interface View extends BaseView {
        void showReply(List<VideoReply> datas);
        void showError();
        void showCount(int count);
        void showNoItem();
    }


    interface Presenter {
        void getReply(int id);
        void getMoreReply();
    }
}
