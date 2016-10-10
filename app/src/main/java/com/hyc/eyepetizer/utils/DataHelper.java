package com.hyc.eyepetizer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.view.LightTopicActivity;
import com.hyc.eyepetizer.view.PagerListActivity;
import com.hyc.eyepetizer.view.RankActivity;
import com.hyc.eyepetizer.view.RecommendsActivity;
import com.hyc.eyepetizer.view.SpecialTopicsActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要负责数据转换工作
 * Created by Administrator on 2016/8/31.
 */
public class DataHelper {
    private static final String RANK_LIST = "ranklist";
    private static final String TAG = "tag";
    private static final String CATEGORY = "category";
    private static final String CAMPAIGN = "campaign";
    private static final String COMMON="common";
    private static final String RECOMMEND="recommend";
    private static final long MINUTE_TIME=60*1000;
    private static final long HOUR_TIME=60*MINUTE_TIME;
    private static final long DAY_TIME=24*HOUR_TIME;
    private static final long WEEK_TIME=DAY_TIME*7;


    /**
     * 转换成对应的种类时间
     */
    public static String getCategoryAndDuration(String Category, int time) {
        StringBuilder builder = new StringBuilder("#");
        builder.append(Category);
        builder.append("  /  ");
        int minute = time / 60;
        if (minute / 10 == 0) {
            builder.append("0");
        }
        builder.append(minute);
        builder.append("\'");
        int second = time % 60;
        if (second / 10 == 0) {
            builder.append("0");
        }
        builder.append(second);
        builder.append("\"");
        return builder.toString();
    }


    /**
     * 用于提取返回的url中的时间
     */
    public static long getTime(String text) {

        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(text);
        while (m.find()) {
            String find = m.group(1).toString();
            long target = Long.valueOf(find);
            if (target > 1000 * 1000 * 1000) {
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
    public static String getCommentTime(long commentTime){
//        Date date=new Date();
//        int year=date.getYear();
//        int month=date.getMonth()+1;
//        int currentDay=date.getDay();
//        Date currentDate=new Date(year,month,currentDay);
        StringBuilder builder=new StringBuilder();
        long currentTime=System.currentTimeMillis();
        long currentDay=currentTime/DAY_TIME;
        long commentDay=commentTime/DAY_TIME;
        if (currentDay == commentDay) {
            Date date=new Date(commentTime);
            int hour=date.getHours();
            int minute=date.getMinutes();
            if (hour==0) {
                builder.append("00:");
            } else if (hour < 10) {
                builder.append("0").append(hour).append(":");
            } else {
                builder.append(hour).append(":");
            }
            if (minute==0) {
                builder.append("00");
            } else if (minute < 10) {
                builder.append("0").append(minute);
            } else {
                builder.append(minute);
            }
        } else if (currentDay-commentDay>7) {
            builder.append("1周前");
        } else {
            builder.append(currentDay-commentDay).append("天前");
        }
//        Date date=new Date(commentTime);
//        Date current=new Date();
//        StringBuilder builder=new StringBuilder();
//        if (date.getYear() == current.getYear()) {
//            if (date.getm) {
//            }
//        } else {
//            builder.append("1周前");
//        }
//
//        Calendar calendar=Calendar.getInstance();
//        calendar.setTime(date);
//       int comment= calendar.get(Calendar.DAY_OF_MONTH);
//        calendar.setTime(current);
//        int currentDay=calendar.get(Calendar.DAY_OF_MONTH);
//        int day=currentDay-comment;
//        if (day==0) {
//            int hour=date.getHours();
//            int minute=date.getMinutes();
//            if (hour==0) {
//                builder.append("00:");
//            } else if (hour < 10) {
//                builder.append("0").append(hour).append(":");
//            } else {
//                builder.append(hour).append(":");
//            }
//            if (minute==0) {
//                builder.append("00");
//            } else if (minute < 10) {
//                builder.append("0").append(minute);
//            } else {
//                builder.append(minute);
//            }
//        } else if (day >= 7) {
//            builder.append("1周前");
//        } else {
//            builder.append(day).append("天前");
//        }
        return builder.toString();
    }

    public static Intent getIntentByUri(Context context, String uri) {
        Uri myUri = Uri.parse(uri);
        String authority = myUri.getAuthority();
        Intent intent = null;
        switch (authority) {
            case RANK_LIST:
                intent = new Intent(context, RankActivity.class);
                break;
            case TAG:
                intent = PagerListActivity.getIntent(context, myUri.getQueryParameter("title"),
                    Integer.valueOf(myUri.getPath().replace("/", "")),
                    FromType.TYPE_TAG_DATE, FromType.TYPE_TAG_SHARE, true);
                break;
            case CATEGORY:
                intent = PagerListActivity.getIntent(context, myUri.getQueryParameter("title"),
                    Integer.valueOf(myUri.getPath().replace("/", "")), FromType.TYPE_CATEGORY_DATE,
                    FromType.TYPE_CATEGORY_SHARE, true);
                break;
            case CAMPAIGN:
                intent=new Intent(context, SpecialTopicsActivity.class);
                break;
            case COMMON:
                String url = myUri.getQueryParameter("url");
                List<String> list = Uri.parse(url).getPathSegments();
                String id = (list.get(list.size() - 1)).replace("/", "");
                intent = LightTopicActivity.getIntent(context, myUri.getQueryParameter("title"),
                    Integer.valueOf(id));
                break;
            case RECOMMEND:
                intent=new Intent(context, RecommendsActivity.class);
                break;
        }
        return intent;
    }

}
