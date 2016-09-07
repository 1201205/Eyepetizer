package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class Reply implements java.io.Serializable {
    private static final long serialVersionUID = -5415564722444611657L;
    private int total;
    private String nextPageUrl;
    private int count;
    private List<VideoReply> replyList;


    public List<VideoReply> getReplyList() {
        return replyList;
    }


    public void setReplyList(List<VideoReply> replyList) {
        this.replyList = replyList;
    }


    public int getTotal() {return this.total;}


    public void setTotal(int total) {this.total = total;}


    public String getNextPageUrl() {return this.nextPageUrl;}


    public void setNextPageUrl(String nextPageUrl) {this.nextPageUrl = nextPageUrl;}


    public int getCount() {return this.count;}


    public void setCount(int count) {this.count = count;}
}
