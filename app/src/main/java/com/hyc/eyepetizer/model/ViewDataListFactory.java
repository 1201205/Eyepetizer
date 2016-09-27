package com.hyc.eyepetizer.model;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ViewDataListFactory {
    /**
     * 根据跳转来源  进行数据获取
     */

    public static VideoListInterface getModel(int type) {
        switch (type) {
            case FromType.TYPE_MAIN:
                return FeedModel.getInstance();
            case FromType.TYPE_DAILY:
                return DailySelectionModel.getInstance();
            case FromType.TYPE_RELATE:
                return VideoRelateModel.getInstance();
            case FromType.TYPE_RANK:
                return VideoRelateModel.getInstance();
            case FromType.TYPE_SECTION:
                return SectionModel.getInstance();
            case FromType.TYPE_HISTORY:
            case FromType.TYPE_MONTH:
            case FromType.TYPE_WEEK:
            case FromType.TYPE_PGC_DATE:
            case FromType.TYPE_PGC_SHARE:
            case FromType.TYPE_TAG_DATE:
            case FromType.TYPE_TAG_SHARE:
            case FromType.TYPE_CATEGORY_DATE:
            case FromType.TYPE_CATEGORY_SHARE:
            case FromType.TYPE_LIGHT_TOPIC:
                return VideoListModel.getInstance();
        }
        return null;
    }
}
