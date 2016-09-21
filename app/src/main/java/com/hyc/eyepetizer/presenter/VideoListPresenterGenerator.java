package com.hyc.eyepetizer.presenter;

import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.FromType;

/**
 * Created by Administrator on 2016/9/9.
 */
public class VideoListPresenterGenerator {

    public static VideoListPresenter generate(int fromType, String tag, int id, VideoListContract.View view) {

        switch (fromType) {
            case FromType.TYPE_HISTORY:
                return new RankPresenter(FromType.TYPE_HISTORY, view, tag);
            case FromType.TYPE_WEEK:
                return new RankPresenter(FromType.TYPE_WEEK, view, tag);
            case FromType.TYPE_MONTH:
                return new RankPresenter(FromType.TYPE_MONTH, view, tag);
            case FromType.TYPE_PGC_DATE:
                return new PgcPresenter(FromType.TYPE_PGC_DATE, id, view, tag);
            case FromType.TYPE_PGC_SHARE:
                return new PgcPresenter(FromType.TYPE_PGC_SHARE, id, view, tag);
            case FromType.TYPE_TAG_DATE:
                return new TagVideoPresenter(FromType.TYPE_TAG_DATE, id, view, tag);
            case FromType.TYPE_TAG_SHARE:
                return new TagVideoPresenter(FromType.TYPE_TAG_SHARE, id, view, tag);
            case FromType.TYPE_CATEGORY_DATE:
                return new CategoryPresenter(FromType.TYPE_TAG_DATE, id, view, tag);
            case FromType.TYPE_CATEGORY_SHARE:
                return new CategoryPresenter(FromType.TYPE_TAG_SHARE, id, view, tag);
        }
        return null;
    }

}
