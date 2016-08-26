package com.hyc.eyepetizer.beans;

public class ViewData implements java.io.Serializable {
    private static final long serialVersionUID = -3841080311073694798L;
    private ItemListData data;
    private String type;


    public ItemListData getData() {return this.data;}


    public void setData(ItemListData data) {this.data = data;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}
}
