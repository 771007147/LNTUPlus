package com.lntuplus.model;

public class TimelineReplyModel {
    private String preid;
    private String id;
    private String number;
    private String name;
    private String text;
    private String datetime;

    public TimelineReplyModel(String preid, String id, String number, String name, String text, String datetime) {
        this.preid = preid;
        this.id = id;
        this.number = number;
        this.name = name;
        this.text = text;
        this.datetime = datetime;
    }

    public String getPreid() {
        return preid;
    }

    public void setPreid(String preid) {
        this.preid = preid;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
