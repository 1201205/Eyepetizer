package com.hyc.eyepetizer.beans;

public class Cover implements java.io.Serializable {
    private static final long serialVersionUID = 1161523434911032334L;
    private String feed;
    private String detail;
    private Object sharing;
    private String blurred;


    public String getFeed() {return this.feed;}


    public void setFeed(String feed) {this.feed = feed;}


    public String getDetail() {return this.detail;}


    public void setDetail(String detail) {this.detail = detail;}


    public Object getSharing() {return this.sharing;}


    public void setSharing(Object sharing) {this.sharing = sharing;}


    public String getBlurred() {return this.blurred;}


    public void setBlurred(String blurred) {this.blurred = blurred;}
}
