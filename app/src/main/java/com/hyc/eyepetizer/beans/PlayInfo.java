package com.hyc.eyepetizer.beans;

public class PlayInfo implements java.io.Serializable {
    private static final long serialVersionUID = -1953592613213925881L;
    private int width;
    private String name;
    private String type;
    private String url;
    private int height;


    public int getWidth() {return this.width;}


    public void setWidth(int width) {this.width = width;}


    public String getName() {return this.name;}


    public void setName(String name) {this.name = name;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}


    public String getUrl() {return this.url;}


    public void setUrl(String url) {this.url = url;}


    public int getHeight() {return this.height;}


    public void setHeight(int height) {this.height = height;}
}
