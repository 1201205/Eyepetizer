package com.hyc.eyepetizer.event;

/**
 * Created by Administrator on 2016/9/5.
 */
public class VideoSelectEvent {

    public int position;
    public int fromType;


    public VideoSelectEvent(int fromType, int position) {
        this.fromType = fromType;
        this.position = position;
    }
}
