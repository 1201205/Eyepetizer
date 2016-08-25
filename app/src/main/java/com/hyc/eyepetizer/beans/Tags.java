package com.hyc.eyepetizer.beans;

public class Tags implements java.io.Serializable {
    private static final long serialVersionUID = -946103001068900264L;
    private String name;
    private String actionUrl;
    private int id;
    private Object adTrack;


    public String getName() {return this.name;}


    public void setName(String name) {this.name = name;}


    public String getActionUrl() {return this.actionUrl;}


    public void setActionUrl(String actionUrl) {this.actionUrl = actionUrl;}


    public int getId() {return this.id;}


    public void setId(int id) {this.id = id;}


    public Object getAdTrack() {return this.adTrack;}


    public void setAdTrack(Object adTrack) {this.adTrack = adTrack;}
}
