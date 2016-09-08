package com.hyc.eyepetizer.model;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ViewDataListFactory {
    /**
     * 根据跳转来源  进行数据获取
     */
    public static final int TYPE_MAIN=1;
    public static final int TYPE_DAILY=2;
    public static final int TYPE_RELATE=3;

    public static VideoListInterface getModel(int type){
        switch (type){
            case TYPE_MAIN:
                return FeedModel.getInstance();
            case TYPE_DAILY:
                break;
            case TYPE_RELATE:
                return VideoRelateModel.getInstance();
        }
        return null;
    }
}
