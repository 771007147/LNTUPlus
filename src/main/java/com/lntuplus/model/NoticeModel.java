package com.lntuplus.model;

public class NoticeModel {
    private int hashcode;
    private int pNo;
    private int rNo;
    private String notices;
    private String datetime;

    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode(int hashcode) {
        this.hashcode = hashcode;
    }

    public int getpNo() {
        return pNo;
    }

    public void setpNo(int pNo) {
        this.pNo = pNo;
    }

    public int getrNo() {
        return rNo;
    }

    public void setrNo(int rNo) {
        this.rNo = rNo;
    }

    public String getNotices() {
        return notices;
    }

    public void setNotices(String notices) {
        this.notices = notices;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
