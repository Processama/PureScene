package com.example.purescene.bean.scenebean;

import java.util.List;

public class SpeLandscape {
    private String areaName;
    private String summary;
    private String address;
    private String name;
    private String content;
    private List<LandscapePic> picList;
    private String attention;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<LandscapePic> getPicList() {
        return picList;
    }

    public void setPicList(List<LandscapePic> picList) {
        this.picList = picList;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }
}
