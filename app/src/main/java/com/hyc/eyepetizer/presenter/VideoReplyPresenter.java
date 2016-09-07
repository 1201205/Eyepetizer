package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoReplyContract;
import com.hyc.eyepetizer.model.beans.Reply;
import com.hyc.eyepetizer.model.beans.VideoReply;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.view.adapter.VideoReplyAdapter;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/7.
 */
public class VideoReplyPresenter extends BasePresenter<VideoReplyContract.View> implements VideoReplyContract.Presenter {

    private int mID;
    private int mLastID;

    public VideoReplyPresenter(VideoReplyContract.View view) {
        super(view);
    }

    @Override
    public void getReply(int id) {
        mID = id;
        Requests.getApi().getReply(id).compose(new DefaultTransformer<Reply>()).subscribe(new Action1<Reply>() {
            @Override
            public void call(Reply reply) {
                if (reply != null && reply.getReplyList() != null) {
                    showReply(reply);
                } else {
                    mView.showNoItem();
                }
            }
        }, new ExceptionAction() {
            @Override
            protected void onNoNetWork() {
                mView.showError();
            }
        });
    }

    @Override
    public void getMoreReply() {
        Requests.getApi().getMoreReply(mID, mLastID, 10).compose(new DefaultTransformer<Reply>()).subscribe(new Action1<Reply>() {
            @Override
            public void call(Reply reply) {
                if (reply != null && reply.getReplyList() != null) {
                    showReply(reply);
                }
            }
        }, new ExceptionAction());
    }

    private void showReply(Reply reply) {
        List<VideoReply> list = reply.getReplyList();
        mLastID = list.get(list.size() - 1).getSequence();
        if (TextUtils.isEmpty(reply.getNextPageUrl())) {
            list.add(new VideoReply(VideoReplyAdapter.EMPTY));
        }
        mView.showReply(list);
    }
}
