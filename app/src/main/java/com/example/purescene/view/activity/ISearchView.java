package com.example.purescene.view.activity;

import android.app.Activity;

import com.example.purescene.bean.scenebean.SpeLandscape;

import java.util.List;

public interface ISearchView {
    public void setXrecyclerView(List<SpeLandscape> speLandscapes);
    public Activity getTheActivity();
    public void notifyData();
}
