package com.hyc.eyepetizer.beans;

public class Consumption implements java.io.Serializable {
    private static final long serialVersionUID = 8936998725039799638L;
    private int shareCount;
    private int replyCount;
    private int collectionCount;


    public int getShareCount() {return this.shareCount;}


    public void setShareCount(int shareCount) {this.shareCount = shareCount;}


    public int getReplyCount() {return this.replyCount;}


    public void setReplyCount(int replyCount) {this.replyCount = replyCount;}


    public int getCollectionCount() {return this.collectionCount;}


    public void setCollectionCount(int collectionCount) {this.collectionCount = collectionCount;}
}
