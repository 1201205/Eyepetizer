package com.hyc.eyepetizer.event;

/**
 * Created by Administrator on 2016/9/5.
 */
public class StartVideoEvent {
    public int[] location;
    //传递的两个参数
    public int parentIndex;
    public int index;
    public String url;
    //用于计算当前开始位置
    public int position;
    public StartVideoEvent(int[] location, int parentIndex, int index, String url,int position) {
        this.location = location;
        this.parentIndex = parentIndex;
        this.index = index;
        this.url = url;
        this.position=position;
    }



}