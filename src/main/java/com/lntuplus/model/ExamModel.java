package com.lntuplus.model;

import java.util.Date;

public class ExamModel implements Comparable<ExamModel> {
    private int number;
    private String course;
    private String term;
    private Date dateTime;
    private String date;
    private String time;
    private String address;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int compareTo(ExamModel o) {
        if (this.getDateTime().before(o.getDateTime())) {
            return 1;
        }
        if (this.getDateTime().after(o.getDateTime())) {
            return -1;
        }
        return 0;
    }
}
