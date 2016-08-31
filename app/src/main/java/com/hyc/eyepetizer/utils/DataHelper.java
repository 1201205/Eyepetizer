package com.hyc.eyepetizer.utils;

/**
 * 主要负责数据转换工作
 * Created by Administrator on 2016/8/31.
 */
public class DataHelper {
    /**
     * 转换成对应的种类时间
     * @param Category
     * @param time
     * @return
     */
    public static String getCategoryAndDuration(String Category,int time){
        StringBuilder builder=new StringBuilder("#");
        builder.append(Category);
        builder.append("  /  ");
        int minute=time/60;
        if (minute/10==0) {
            builder.append("0");
        }
        builder.append(minute);
        builder.append("\'");
        int second=time%60;
        if (second/10==0) {
            builder.append("0");
        }
        builder.append(second);
        builder.append("\"");
        return builder.toString();
    }
}
