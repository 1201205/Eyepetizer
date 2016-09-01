package com.hyc.eyepetizer.utils;

/**
 * Created by Administrator on 2016/9/1.
 */
public class DateUtil {

    public static int getHour(long time){
        return (int) (time/1000/60/60);
    }
    public static int getMinute(long time){
        return (int) (time/1000/60);
    }
}
