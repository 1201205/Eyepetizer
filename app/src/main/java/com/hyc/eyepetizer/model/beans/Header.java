package com.hyc.eyepetizer.model.beans;

public class Header implements java.io.Serializable {
    private static final long serialVersionUID = 8067679167771620994L;
    private FooterData data;
    private String type;


    public FooterData getData() {return this.data;}


    public void setData(FooterData data) {this.data = data;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}
}
