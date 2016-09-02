package com.hyc.eyepetizer.model.beans;

public class Author implements java.io.Serializable {
    private static final long serialVersionUID = -493082693763474811L;
    private String icon;
    private String name;
    private String link;
    private String description;
    private long latestReleaseTime;
    private int id;
    private int videoNum;
    private Object adTrack;


    public String getIcon() {return this.icon;}


    public void setIcon(String icon) {this.icon = icon;}


    public String getName() {return this.name;}


    public void setName(String name) {this.name = name;}


    public String getLink() {return this.link;}


    public void setLink(String link) {this.link = link;}


    public String getDescription() {return this.description;}


    public void setDescription(String description) {this.description = description;}


    public long getLatestReleaseTime() {return this.latestReleaseTime;}


    public void setLatestReleaseTime(long latestReleaseTime) {
        this.latestReleaseTime = latestReleaseTime;
    }


    public int getId() {return this.id;}


    public void setId(int id) {this.id = id;}


    public int getVideoNum() {return this.videoNum;}


    public void setVideoNum(int videoNum) {this.videoNum = videoNum;}


    public Object getAdTrack() {return this.adTrack;}


    public void setAdTrack(Object adTrack) {this.adTrack = adTrack;}
}
