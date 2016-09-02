package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class Selection implements java.io.Serializable {
    private static final long serialVersionUID = -9047316804998833030L;
    private long date;
    private long nextPublishTime;
    private Object dialog;
    private String nextPageUrl;
    private int count;
    private List<SectionList> sectionList;


    public long getDate() {return this.date;}


    public void setDate(long date) {this.date = date;}


    public long getNextPublishTime() {return this.nextPublishTime;}


    public void setNextPublishTime(long nextPublishTime) {this.nextPublishTime = nextPublishTime;}


    public Object getDialog() {return this.dialog;}


    public void setDialog(Object dialog) {this.dialog = dialog;}


    public String getNextPageUrl() {return this.nextPageUrl;}


    public void setNextPageUrl(String nextPageUrl) {this.nextPageUrl = nextPageUrl;}


    public int getCount() {return this.count;}


    public void setCount(int count) {this.count = count;}


    public List<SectionList> getSectionList() {return this.sectionList;}


    public void setSectionList(List<SectionList> sectionList) {this.sectionList = sectionList;}
}
