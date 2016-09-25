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
            case Type.NO_MORE:
                return ViewType.NO_MORE;
            case Type.CAMPAIGN:
                return ViewType.CAMPAIGN;
            case Type.BRIEF_CARD:
                return ViewType.BRIEF_CARD;
            case Type.HORIZONTAL_SCROLL_CARD:
                return ViewType.HORIZONTAL_SCROLL_CARD;
            case Type.SQUARE_CARD:
                return ViewType.SQUARE_CARD;
            case Type.RECTANGLE_CARD:
                return ViewType.RECTANGLE_CARD;
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
        String CAMPAIGN = "campaign";//有标题的水平视频
        String NO_MORE = "noMore";
        String BRIEF_CARD = "briefCard";
        String HORIZONTAL_SCROLL_CARD = "horizontalScrollCard";
        String SQUARE_CARD = "squareCard";
        String RECTANGLE_CARD = "rectangleCard";

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
        int NO_MORE = 8;
        int CAMPAIGN = 9;
        int BRIEF_CARD = 10;
        int HORIZONTAL_SCROLL_CARD = 11;
        int SQUARE_CARD = 12;
        int RECTANGLE_CARD = 13;
    }
}
