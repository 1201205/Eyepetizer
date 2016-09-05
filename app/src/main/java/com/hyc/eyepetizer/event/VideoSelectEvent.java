package com.hyc.eyepetizer.event;

/**
 * Created by Administrator on 2016/9/5.
 */
public class VideoSelectEvent {

    public int position;
    public String url;
    public boolean hasScrolled;

    public VideoSelectEvent(int position,String url,boolean hasScrolled) {
        this.position = position;
        this.url=url;
        this.hasScrolled=hasScrolled;
    }
}
