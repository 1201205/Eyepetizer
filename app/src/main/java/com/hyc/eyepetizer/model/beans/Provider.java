package com.hyc.eyepetizer.model.beans;

public class Provider implements java.io.Serializable {
    private static final long serialVersionUID = 9209016264480301096L;
    private String name;
    private String icon;
    private String alias;


    public String getName() {return this.name;}


    public void setName(String name) {this.name = name;}


    public String getIcon() {return this.icon;}


    public void setIcon(String icon) {this.icon = icon;}


    public String getAlias() {return this.alias;}


    public void setAlias(String alias) {this.alias = alias;}
}
