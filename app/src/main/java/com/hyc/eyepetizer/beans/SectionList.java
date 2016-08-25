package com.hyc.eyepetizer.beans;

import java.util.ArrayList;
import java.util.List;

public class SectionList implements java.io.Serializable {
    private static final long serialVersionUID = 316505256641919188L;
    private Footer footer;
    private int count;
    private Header header;
    private List<ItemList> itemList;
    private int id;
    private String type;
    private Object adTrack;


    public Footer getFooter() {return this.footer;}


    public void setFooter(Footer footer) {this.footer = footer;}


    public int getCount() {return this.count;}


    public void setCount(int count) {this.count = count;}


    public Header getHeader() {return this.header;}


    public void setHeader(Header header) {this.header = header;}


    public List<ItemList> getItemList() {return this.itemList;}


    public void setItemList(List<ItemList> itemList) {this.itemList = itemList;}


    public int getId() {return this.id;}


    public void setId(int id) {this.id = id;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}


    public Object getAdTrack() {return this.adTrack;}


    public void setAdTrack(Object adTrack) {this.adTrack = adTrack;}
}
