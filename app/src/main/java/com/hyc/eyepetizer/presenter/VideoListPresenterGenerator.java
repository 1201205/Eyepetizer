package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.FromType;

/**
 * Created by Administrator on 2016/9/9.
 */
public class VideoListPresenterGenerator {

    public static VideoListPresenter generate(int fromType, String tag, VideoListContract.View view) {

        switch (fromType) {
            case FromType.TYPE_HISTORY:
                return new RankPresenter(FromType.TYPE_HISTORY, view, tag);
            case FromType.TYPE_WEEK:
                return new RankPresenter(FromType.TYPE_WEEK, view, tag);
            case FromType.TYPE_MONTH:
                return new RankPresenter(FromType.TYPE_MONTH, view, tag);
        }
        return null;
    }

}
