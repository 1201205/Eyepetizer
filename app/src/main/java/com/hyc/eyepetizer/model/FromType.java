package com.hyc.eyepetizer.model;

/**
 * Created by ray on 16/9/8.
 */
public interface FromType {
    int TYPE_MAIN = 1;
    int TYPE_DAILY = 2;
    int TYPE_RELATE = 3;
    int TYPE_RANK = 4;
    int TYPE_WEEK = -5;
    int TYPE_MONTH = -6;
    int TYPE_HISTORY = -7;
    int TYPE_PGC_DATE = -8;
    int TYPE_PGC_SHARE = -9;
    int TYPE_TAG_DATE = -10;
    int TYPE_TAG_SHARE = -11;
    int TYPE_CATEGORY_DATE = -12;
    int TYPE_CATEGORY_SHARE = -13;

    interface Tag {
        String RANK_WEEK = "weekly";
        String RANK_MONTH = "monthly";
        String RANK_HISTORY = "historical";
        String DATE = "date";
        String SHARE_COUNT="shareCount";
    }
}
