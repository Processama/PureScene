package com.example.purescene.bean.scenebean;

import java.util.List;

public class Landscape {
    private double allPages;
    private List<SpeLandscape> contentlist;

    public double getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public List<SpeLandscape> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<SpeLandscape> contentlist) {
        this.contentlist = contentlist;
    }
}
