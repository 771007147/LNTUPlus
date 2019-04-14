package com.lntuplus.model;

public class SignModel {
    private int number;
    private String name;
    private String iClass;
    private String day;
    private int no;
    private String signTime;

    public SignModel() {
    }

    ;

    public SignModel(int number, String day, int no) {
        this.number = number;
        this.day = day;
        this.no = no;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}
