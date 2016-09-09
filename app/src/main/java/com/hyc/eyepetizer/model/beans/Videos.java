package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class Videos {
    private int total;
    private String nextPageUrl;
    private int count;

    public List<ViewData> getItemList() {
        return itemList;
    }

    public void setItemList(List<ViewData> itemList) {
        this.itemList = itemList;
    }

    private List<ViewData> itemList;

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
