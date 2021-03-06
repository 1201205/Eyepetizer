package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class ItemListData implements java.io.Serializable {
    private static final long serialVersionUID = -6500092217556178025L;
    private long date;
    private Object shareAdTrack;
    private long releaseTime;
    private String description;
    private String title;
    private String type;
    private Object favoriteAdTrack;
    private Object waterMarks;
    private String playUrl;
    private Cover cover;
    private int duration;
    private Provider provider;
    private int id;
    private Object adTrack;
    private Author author;
    private String dataType;
    private Consumption consumption;
    private Label label;
    private List<Tags> tags;
    private Object webAdTrack;
    private List<PlayInfo> playInfo;
    private WebUrl webUrl;
    private Object campaign;
    private String category;
    private int idx;
    private String text;
    private CoverHeader header;
    private List<ViewData> itemList;
    private int count;
    private String icon;
    private String subTitle;
    private String image;
    private int height;
    private String font;
    private String actionUrl;
    private Object promotion;

    public boolean isShade() {
        return shade;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    private boolean shade;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public CoverHeader getHeader() {
        return header;
    }

    public void setHeader(CoverHeader header) {
        this.header = header;
    }

    public List<ViewData> getItemList() {
        return itemList;
    }

    public void setItemList(List<ViewData> itemList) {
        this.itemList = itemList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public long getDate() {return this.date;}


    public void setDate(long date) {this.date = date;}


    public Object getShareAdTrack() {return this.shareAdTrack;}


    public void setShareAdTrack(Object shareAdTrack) {this.shareAdTrack = shareAdTrack;}


    public long getReleaseTime() {return this.releaseTime;}


    public void setReleaseTime(long releaseTime) {this.releaseTime = releaseTime;}


    public String getDescription() {return this.description;}


    public void setDescription(String description) {this.description = description;}


    public String getTitle() {return this.title;}


    public void setTitle(String title) {this.title = title;}


    public String getType() {return this.type;}


    public void setType(String type) {this.type = type;}


    public Object getFavoriteAdTrack() {return this.favoriteAdTrack;}


    public void setFavoriteAdTrack(Object favoriteAdTrack) {this.favoriteAdTrack = favoriteAdTrack;}


    public Object getWaterMarks() {return this.waterMarks;}


    public void setWaterMarks(Object waterMarks) {this.waterMarks = waterMarks;}


    public String getPlayUrl() {return this.playUrl;}


    public void setPlayUrl(String playUrl) {this.playUrl = playUrl;}


    public Cover getCover() {return this.cover;}


    public void setCover(Cover cover) {this.cover = cover;}


    public int getDuration() {return this.duration;}


    public void setDuration(int duration) {this.duration = duration;}


    public Provider getProvider() {return this.provider;}


    public void setProvider(Provider provider) {
        this.provider = provider;
    }


    public int getId() {return this.id;}


    public void setId(int id) {this.id = id;}


    public Object getAdTrack() {return this.adTrack;}


    public void setAdTrack(Object adTrack) {this.adTrack = adTrack;}


    public Author getAuthor() {return this.author;}


    public void setAuthor(Author author) {this.author = author;}


    public String getDataType() {return this.dataType;}


    public void setDataType(String dataType) {this.dataType = dataType;}


    public Consumption getConsumption() {return this.consumption;}


    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }


    public Label getLabel() {return this.label;}


    public void setLabel(Label label) {this.label = label;}


    public List<Tags> getTags() {return this.tags;}


    public void setTags(List<Tags> tags) {this.tags = tags;}


    public Object getWebAdTrack() {return this.webAdTrack;}


    public void setWebAdTrack(Object webAdTrack) {this.webAdTrack = webAdTrack;}


    public List<PlayInfo> getPlayInfo() {return this.playInfo;}


    public void setPlayInfo(List<PlayInfo> playInfo) {
        this.playInfo = playInfo;
    }


    public WebUrl getWebUrl() {return this.webUrl;}


    public void setWebUrl(WebUrl webUrl) {this.webUrl = webUrl;}


    public Object getCampaign() {return this.campaign;}


    public void setCampaign(Object campaign) {this.campaign = campaign;}


    public String getCategory() {return this.category;}


    public void setCategory(String category) {this.category = category;}


    public int getIdx() {return this.idx;}


    public void setIdx(int idx) {this.idx = idx;}


    public Object getPromotion() {return this.promotion;}


    public void setPromotion(Object promotion) {this.promotion = promotion;}
}
