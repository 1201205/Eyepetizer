package com.hyc.eyepetizer.event;

/**
 * Created by Administrator on 2016/9/5.
 */
public class VideoClickedEvent {
    public int[] loaction;
    public int parentIndex;
    public int index;
    public String url;

    public VideoClickedEvent(int[] loaction, int parentIndex, int index, String url) {
        this.loaction = loaction;
        this.parentIndex = parentIndex;
        this.index = index;
        this.url = url;
    }
}
