package com.example.purescene.view.scene;

import android.app.Activity;

import com.example.purescene.bean.scenebean.SpeLandscape;

import java.util.List;

public interface ISceneView {
    public void setViewFirstly(List<SpeLandscape> speLandscapes);
    public void setBanner(List<String> images);
    public Activity getTheActivity();
    public void notifyData();
}
