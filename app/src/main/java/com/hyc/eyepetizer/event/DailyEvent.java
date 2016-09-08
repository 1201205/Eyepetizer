package com.hyc.eyepetizer.event;

/**
 * Created by ray on 16/9/8.
 */
public class DailyEvent {
    public int locationY;
    //传递的两个参数
    public int parentIndex;
    public int index;
    public String url;
    //用于计算当前开始位置
    public int position;


    public DailyEvent(int locationY, int parentIndex, int index, String url, int position) {
        this.locationY = locationY;
        this.parentIndex = parentIndex;
        this.index = index;
        this.url = url;
        this.position = position;
    }

}
