package com.hyc.eyepetizer.beans;

public class FooterData implements java.io.Serializable {
    private static final long serialVersionUID = -3845362898284175373L;
    private String actionUrl;
    private String text;
    private Object adTrack;
    private String font;


    public String getActionUrl() {return this.actionUrl;}


    public void setActionUrl(String actionUrl) {this.actionUrl = actionUrl;}


    public String getText() {return this.text;}


    public void setText(String text) {this.text = text;}


    public Object getAdTrack() {return this.adTrack;}


    public void setAdTrack(Object adTrack) {this.adTrack = adTrack;}


    public String getFont() {return this.font;}


    public void setFont(String font) {this.font = font;}
}
