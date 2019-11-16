package com.lntuplus.model;

public class SignModel {
    private int number;
    private String name;
    private String iClass;
    private String date;
    private int index;
    private String signTime;

    public SignModel() {
    }

    public SignModel(int number, String date, int index) {
        this.number = number;
        this.date = date;
        this.index = index;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getiClass() {
        return iClass;
    }

    public void setiClass(String iClass) {
        this.iClass = iClass;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}
