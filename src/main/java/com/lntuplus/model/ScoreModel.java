package com.lntuplus.model;

import java.io.Serializable;
import java.util.Date;
import java.util.function.DoubleBinaryOperator;

public class ScoreModel implements Comparable<ScoreModel>, Serializable {
    private String number;
    private String course;
    private String mode;
    private String year;
    private String score;
    private double credit;
    private String cNumber;
    private int cNo;
    private String examType;
    private String properties;
    private String remark;
    private String delayed;
    private String openUnit;
    private String teacher;
    private String scoreInfo;
    private String normalScore;
    private String midScore;
    private String endScore;
    private int examRoom;
    private int seatNum;
    private Date datetime;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getcNumber() {
        return cNumber;
    }

    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    public int getcNo() {
        return cNo;
    }

    public void setcNo(int cNo) {
        this.cNo = cNo;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelayed() {
        return delayed;
    }

    public void setDelayed(String delayed) {
        this.delayed = delayed;
    }

    public String getOpenUnit() {
        return openUnit;
    }

    public void setOpenUnit(String openUnit) {
        this.openUnit = openUnit;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getScoreInfo() {
        return scoreInfo;
    }

    public void setScoreInfo(String scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public String getNormalScore() {
        return normalScore;
    }

    public void setNormalScore(String normalScore) {
        this.normalScore = normalScore;
    }

    public String getMidScore() {
        return midScore;
    }

    public void setMidScore(String midScore) {
        this.midScore = midScore;
    }

    public String getEndScore() {
        return endScore;
    }

    public void setEndScore(String endScore) {
        this.endScore = endScore;
    }

    public int getExamRoom() {
        return examRoom;
    }

    public void setExamRoom(int examRoom) {
        this.examRoom = examRoom;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public int compareTo(ScoreModel o) {
        int yearA = Integer.parseInt(this.getYear().substring(0, 4));
        String termA = this.getYear().substring(4);
        double scoreA = transformScore(this.getScore());
        int yearB = Integer.parseInt(o.getYear().substring(0, 4));
        String termB = o.getYear().substring(4);
        double scoreB = transformScore(o.getScore());
        if (yearA > yearB) {
            return -1;
        }
        if (yearA < yearB) {
            return 1;
        }
        if (termA.equals("秋") && termB.equals("春")) {
            return -1;
        }
        if (termA.equals("春") && termB.equals("秋")) {
            return 1;
        }
        if (scoreA > scoreB) {
            return -1;
        }
        if (scoreB > scoreA) {
            return 1;
        }
        return 0;
    }

    private double transformScore(String value) {
        double v;
        switch (value) {
            case "":
                v = 0;
                break;
            case "优秀":
                v = 95;
                break;
            case "优":
                v = 95;
                break;
            case "良":
                v = 85;
                break;
            case "中":
                v = 75;
                break;
            case "及格":
                v = 60;
                break;
            case "合格":
                v = 85;
                break;
            case "不及格":
                v = 0;
                break;
            case "不合格":
                v = 0;
                break;
            case "实践成绩未提交":
                v = 0;
                break;
            default:
                v = Double.valueOf(value);
        }
        return v;
    }
}
