package com.lntuplus.model;

public class TimelineMessageModel {
    private String id;
    private String number;
    private int type;
    private String name;
    private String text;
    private String imgUrl;
    private String datetime;

    public TimelineMessageModel(String id, String number, int type, String name, String text, String imgUrl, String datetime) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.name = name;
        this.text = text;
        this.imgUrl = imgUrl;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
