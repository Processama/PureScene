package com.example.purescene.bean.scenebean;

import java.util.List;

public class Landscape {
    int allPages;
    List<SpeLandscape> contentlist;

    public int getAllPages() {
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
