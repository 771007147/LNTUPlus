package com.lntuplus.model;

public class TableModel {
    private String course;
    private String time;
    private String teacher;
    private String address;
    private String singleDouble;
    private String start;
    private String end;

    public void setNull() {
        this.course = new String();
        this.time = "";
        this.teacher = "";
        this.address = "";
        this.singleDouble = "";
        this.start = "";
        this.end = "";
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSingleDouble() {
        return singleDouble;
    }

    public void setSingleDouble(String singleDouble) {
        this.singleDouble = singleDouble;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
