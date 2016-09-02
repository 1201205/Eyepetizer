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

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0
                ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds);
    }
}
