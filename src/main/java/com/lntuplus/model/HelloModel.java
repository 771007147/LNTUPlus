package com.lntuplus.model;

import java.io.Serializable;

public class HelloModel implements Serializable {
    private String updateTime;
    private String title;
    private String imgUrl_16;
    private String imgUrl_21;
    private String url;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl_16() {
        return imgUrl_16;
    }

    public void setImgUrl_16(String imgUrl_16) {
        this.imgUrl_16 = imgUrl_16;
    }

    public String getImgUrl_21() {
        return imgUrl_21;
    }

    public void setImgUrl_21(String imgUrl_21) {
        this.imgUrl_21 = imgUrl_21;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
