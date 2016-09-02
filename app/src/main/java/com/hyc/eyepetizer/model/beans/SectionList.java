package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class SectionList implements java.io.Serializable {
    private static final long serialVersionUID = 316505256641919188L;
    private ViewData footer;
    private int count;
    private ViewData header;
    private List<ViewData> itemList;
    private int id;
    private String type;
    private Object adTrack;


    public ViewData getFooter() {return this.footer;}


    public void setFooter(ViewData footer) {this.footer = footer;}


    public int getCount() {return this.count;}


    public void setCount(int count) {this.count = count;}


    public ViewData getHeader() {return this.header;}


    public void setHeader(ViewData header) {this.header = header;}


    public List<ViewData> getItemList() {return this.itemList;}


    public void setItemList(List<ViewData> itemList) {this.itemList = itemList;}


    public int getId() {return this.id;}


    public void setId(int id) {this.id = id;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}


    public Object getAdTrack() {return this.adTrack;}


    public void setAdTrack(Object adTrack) {this.adTrack = adTrack;}
}
