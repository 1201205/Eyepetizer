package com.hyc.eyepetizer.utils;

/**
 * Created by Administrator on 2016/8/26.
 */
public class WidgetHelper {
    //VideoBeanForClient--首页视频
    public static int getViewType(String type) {
        switch (type) {
            case Type.VIDEO:
                return ViewType.VIDEO;
            case Type.FORWARD_FOOTER:
                return ViewType.FORWARD_FOOTER;
            case Type.VIDEO_COLLECTION_WITH_COVER:
                return ViewType.VIDEO_COLLECTION_WITH_COVER;
            case Type.BLANK_FOOTER:
                return ViewType.BLANK_FOOTER;
            case Type.TEXT_HEADER:
                return ViewType.TEXT_HEADER;
            case Type.VIDEO_COLLECTION_WITH_BRIEF:
                return ViewType.VIDEO_COLLECTION_WITH_BRIEF;
            case Type.VIDEO_COLLECTION_WITH_TITLE:
                return ViewType.VIDEO_COLLECTION_WITH_TITLE;
        }
        return ViewType.Error;
    }

    public interface Type {
        //BLANK_FOOTER
        String VIDEO = "video";//首页单个精选视频
        String FORWARD_FOOTER = "forwardFooter";//到精选视频
        String VIDEO_COLLECTION_WITH_COVER = "videoCollectionWithCover";//水平编辑视频
        String BLANK_FOOTER = "blankFooter";//空白分割  有对应高度
        String TEXT_HEADER = "textHeader";//标题
        String VIDEO_COLLECTION_WITH_BRIEF = "videoCollectionWithBrief";//水平作者视频
        String VIDEO_COLLECTION_WITH_TITLE = "videoCollectionWithTitle";//有标题的水平视频
    }

    public interface ViewType {
        int Error = -1;
        int VIDEO = 1;
        int FORWARD_FOOTER = 2;
        int VIDEO_COLLECTION_WITH_COVER = 3;
        int BLANK_FOOTER = 4;
        int TEXT_HEADER = 5;
        int VIDEO_COLLECTION_WITH_BRIEF = 6;
        int VIDEO_COLLECTION_WITH_TITLE = 7;
    }
}
