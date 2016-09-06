package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class VideoRelated implements java.io.Serializable {
    private static final long serialVersionUID = -5415564722044611657L;
    private int total;
    private String nextPageUrl;
    private int count;
    private List<ViewData> itemList;


    public List<ViewData> getItemList() {
        return itemList;
    }


    public void setItemList(List<ViewData> itemList) {
        this.itemList = itemList;
    }


    public int getTotal() {return this.total;}


    public void setTotal(int total) {this.total = total;}


    public String getNextPageUrl() {return this.nextPageUrl;}


    public void setNextPageUrl(String nextPageUrl) {this.nextPageUrl = nextPageUrl;}


    public int getCount() {return this.count;}


    public void setCount(int count) {this.count = count;}
}
