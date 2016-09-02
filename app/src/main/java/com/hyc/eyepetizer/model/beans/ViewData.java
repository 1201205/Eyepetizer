package com.hyc.eyepetizer.model.beans;

public class ViewData implements java.io.Serializable {
    private static final long serialVersionUID = -3841080311073694798L;
    private ItemListData data;
    private String type;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    private int parentIndex;
    public ViewData(ItemListData data, String type) {
        this.data = data;
        this.type = type;
    }

    public ItemListData getData() {return this.data;}


    public void setData(ItemListData data) {this.data = data;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}
}
