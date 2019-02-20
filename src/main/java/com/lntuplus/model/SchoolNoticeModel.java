package com.lntuplus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchoolNoticeModel {
    private String success = "null";
    private List<List<Map>> noticeMains;
    private int hashcode;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Map> getNoitceItem(int index) {
        return noticeMains.get(index);
    }

    public void addNoticeMain(List<Map> noticeMain) {
        if (noticeMains == null) {
            noticeMains = new ArrayList<>();
        }
        this.noticeMains.add(noticeMain);
    }

    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode() {
        this.hashcode = noticeMains.hashCode();
    }
}
