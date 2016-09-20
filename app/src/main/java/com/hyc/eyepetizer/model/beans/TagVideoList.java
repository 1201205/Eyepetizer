package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class TagVideoList {
    private List<ViewData> itemList;
    private int total;
    private String nextPageUrl;
    private int count;

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public List<ViewData> getItemList() {
        return itemList;
    }

    public void setItemList(List<ViewData> itemList) {
        this.itemList = itemList;
    }

    private Tags tag;

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNextPageUrl() {
        return this.nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
