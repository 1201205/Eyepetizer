package com.hyc.eyepetizer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hyc.eyepetizer.event.HomePageEvent;
import com.hyc.eyepetizer.event.RouterWrapper;
import com.hyc.eyepetizer.model.FromType;
import com.hyc.eyepetizer.view.LightTopicActivity;
import com.hyc.eyepetizer.view.PagerListActivity;
import com.hyc.eyepetizer.view.RankActivity;
import com.hyc.eyepetizer.view.RecommendsActivity;
import com.hyc.eyepetizer.view.SelectionActivity;
import com.hyc.eyepetizer.view.SpecialTopicsActivity;
import com.hyc.eyepetizer.view.WebViewActivity;

import java.util.Date;
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
    private static final String COMMON = "common";
    private static final String RECOMMEND = "recommend";
    private static final String HOME_PAGE = "homepage";
    private static final String FEED = "feed";
    private static final String WEBVIEW="webview";

    private static final long MINUTE_TIME = 60 * 1000;
    private static final long HOUR_TIME = 60 * MINUTE_TIME;
    private static final long DAY_TIME = 24 * HOUR_TIME;
    private static final long WEEK_TIME = DAY_TIME * 7;


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
        Uri uri = Uri.parse(text);
        List<String> segments = uri.getPathSegments();
        return Integer.valueOf(segments.get(segments.size() - 1));
    }

    public static String getCommentTime(long commentTime) {
        StringBuilder builder = new StringBuilder();
        long currentTime = System.currentTimeMillis();
        long time = currentTime - commentTime;
        if (time >= WEEK_TIME) {
            builder.append("1星期前");
        } else if (time >= DAY_TIME) {
            builder.append(time / DAY_TIME).append("天前");
        } else {
            Date date = new Date(commentTime);
            Date currentDate = new Date();
            if (currentDate.getDate() != date.getDate()) {
                builder.append("1天前");
            } else {
                int hour = date.getHours();
                int minute = date.getMinutes();
                if (hour < 10) {
                    builder.append("0").append(hour);
                } else {
                    builder.append(hour);
                }
                builder.append(":");
                if (minute < 10) {
                    builder.append("0").append(minute);
                } else {
                    builder.append(minute);
                }
            }
        }
        return builder.toString();
    }


    /**
     * 不想再次解析uri  所以把两个包裹在一起
     */
    public static RouterWrapper getIntentByUri(Context context, String uri) {
        Uri myUri = Uri.parse(uri);
        String authority = myUri.getAuthority();
        RouterWrapper wrapper = new RouterWrapper(null, null);
        Intent intent = null;
        HomePageEvent event = null;
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
                intent = new Intent(context, SpecialTopicsActivity.class);
                break;
            case COMMON:
                String url = myUri.getQueryParameter("url");
                List<String> list = Uri.parse(url).getPathSegments();
                String id = (list.get(list.size() - 1)).replace("/", "");
                intent = LightTopicActivity.getIntent(context, myUri.getQueryParameter("title"),
                        Integer.valueOf(id));
                break;
            case RECOMMEND:
                intent = new Intent(context, RecommendsActivity.class);
                break;
            case FEED:
                intent = new Intent(context, SelectionActivity.class);
            case HOME_PAGE:
                event = new HomePageEvent(myUri.getPath().replace("/", ""));
                break;
            case WEBVIEW:
                intent= WebViewActivity.getIntent(context,myUri.getQueryParameter("title"),myUri.getQueryParameter("url"));
        }
        wrapper.setIntent(intent);
        wrapper.setEvent(event);
        return wrapper;
    }

}
