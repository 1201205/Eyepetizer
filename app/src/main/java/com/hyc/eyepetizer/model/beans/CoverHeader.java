package com.hyc.eyepetizer.model.beans;

public class CoverHeader {
    private String cover;
    private String actionUrl;
    private int id;
    private String title;
    private String font;
    private String icon;
    private String subTitle;
    private String description;
    private Object adTrack;

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Object getAdTrack() {
        return adTrack;
    }


    public void setAdTrack(Object adTrack) {
        this.adTrack = adTrack;
    }


    public String getIco() {
        return icon;
    }


    public void setIco(String icon) {
        this.icon = icon;
    }


    public String getSubTitle() {
        return subTitle;
    }


    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getFont() {
        return this.font;
    }

    public void setFont(String font) {
        this.font = font;
    }
}
