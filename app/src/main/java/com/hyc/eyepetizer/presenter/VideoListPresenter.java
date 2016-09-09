package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.contract.VideoListContract;

/**
 * Created by Administrator on 2016/9/9.
 */
public abstract class VideoListPresenter extends BasePresenter<VideoListContract.View> {
    public VideoListPresenter(VideoListContract.View view) {
        super(view);
    }

    public abstract void getVideoList();
}
