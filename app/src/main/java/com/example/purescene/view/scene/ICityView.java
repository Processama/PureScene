package com.example.purescene.view.scene;

import android.app.Activity;

import java.util.List;

public interface ICityView {
    public Activity getTheActivity();
    public void queryFirstly(List<String> province);
    public void queryProvince();
    public void queryCity(String cityTitle);
    public void setViewClickListener();
}
