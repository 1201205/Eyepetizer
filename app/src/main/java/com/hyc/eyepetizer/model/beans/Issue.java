package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class Issue {
    private long date;
    private long publishTime;
    private long releaseTime;
    private int count;
    private String type;

    public List<ViewData> getItemList() {
        return itemList;
    }

    public void setItemList(List<ViewData> itemList) {
        this.itemList = itemList;
    }

    private List<ViewData> itemList;
    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
