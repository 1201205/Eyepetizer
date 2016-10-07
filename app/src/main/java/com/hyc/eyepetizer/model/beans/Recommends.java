package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class Recommends implements java.io.Serializable {
    private static final long serialVersionUID = -1472101648018695488L;
    private long nextPublishTime;
    private int total;
    private int count;
    private List<ViewData> itemList;


    public List<ViewData> getItemList() {
        return itemList;
    }


    public void setItemList(List<ViewData> itemList) {
        this.itemList = itemList;
    }


    public long getNextPublishTime() {return this.nextPublishTime;}


    public void setNextPublishTime(long nextPublishTime) {this.nextPublishTime = nextPublishTime;}


    public int getTotal() {return this.total;}


    public void setTotal(int total) {this.total = total;}


    public int getCount() {return this.count;}


    public void setCount(int count) {this.count = count;}
}
