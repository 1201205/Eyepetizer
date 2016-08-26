package com.hyc.eyepetizer.utils;

/**
 * Created by Administrator on 2016/8/26.
 */
public class WidgetHelper {
    //VideoBeanForClient--首页视频
    public static int getViewType(String type){
        switch (type){
            case Type.VEDIO:
                return ViewType.VEDIO;
            case Type.forwardFooter:
                return ViewType.forwardFooter;
            case Type.videoCollectionWithCover:
                return ViewType.videoCollectionWithCover;
            case Type.blankFooter:
                return ViewType.blankFooter;
            case Type.textHeader:
                return ViewType.textHeader;
            case Type.videoCollectionWithBrief:
                return ViewType.videoCollectionWithBrief;
            case Type.videoCollectionWithTitle:
                return ViewType.videoCollectionWithTitle;
        }
        return ViewType.Error;
    }
    public interface Type {
        //blankFooter
        String VEDIO = "video";//首页单个精选视频
        String forwardFooter = "forwardFooter";//到精选视频
        String videoCollectionWithCover = "videoCollectionWithCover";//水平编辑视频
        String blankFooter = "blankFooter";//空白分割  有对应高度
        String authorSection = "authorSection";
        String textHeader = "textHeader";//标题
        String videoCollectionWithBrief = "videoCollectionWithBrief";//水平作者视频
        String videoCollectionWithTitle="videoCollectionWithTitle";//有标题的水平视频
    }
    public interface ViewType{
        int Error=-1;
        int VEDIO=1;
        int forwardFooter=2;
        int videoCollectionWithCover=3;
        int blankFooter=4;
        int textHeader=5;
        int videoCollectionWithBrief=6;
        int videoCollectionWithTitle=7;
    }
}
