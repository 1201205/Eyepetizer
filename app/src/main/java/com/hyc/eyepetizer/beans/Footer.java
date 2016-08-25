package com.hyc.eyepetizer.beans;

public class Footer implements java.io.Serializable {
    private static final long serialVersionUID = 1945866281730010540L;
    private FooterData data;
    private String type;


    public FooterData getData() {return this.data;}


    public void setData(FooterData data) {this.data = data;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}
}
