package com.lntuplus.model;

import java.util.List;
import java.util.Map;

public class ArticleModel {
    private String success;
    private List<Map<String, Object>> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
