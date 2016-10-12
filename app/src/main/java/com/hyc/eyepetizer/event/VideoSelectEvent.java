package com.hyc.eyepetizer.event;

/**
 * Created by Administrator on 2016/9/5.
 */
public class VideoSelectEvent {

    public int position;
    public int fromType;
    public String url;


    public VideoSelectEvent(int fromType, int position,String url) {
        this.fromType = fromType;
        this.position = position;
        this.url = url;
    }
}
