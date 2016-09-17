package com.hyc.eyepetizer.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 用于提取返回的url中的时间
     * @param text
     * @return
     */
    public static long getTime(String text) {

        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(text);
        while (m.find()) {
            String find = m.group(1).toString();
            long target=Long.valueOf(find);
            if (target>1000*1000*1000) {
                return target;
            }
        }
        return 0;
    }


    public static int getID(String text) {

        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(text);
        while (m.find()) {
            String find = m.group(1).toString();
            return Integer.valueOf(find);
        }
        return 0;
    }

}
