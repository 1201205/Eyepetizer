package com.hyc.eyepetizer.event;

/**
 * Created by Administrator on 2016/9/5.
 */
public class VideoDetailBackEvent {

    public int position;
    public String url;
    public boolean hasScrolled;
    public boolean theLast;
    public int fromType;


    public VideoDetailBackEvent(int fromType, int position, String url, boolean hasScrolled, boolean theLast) {
        this.fromType = fromType;
        this.position = position;
        this.url = url;
        this.hasScrolled = hasScrolled;
        this.theLast = theLast;
    }
}
