package com.example.purescene.view.about;

import android.app.Activity;

import com.example.purescene.bean.naturebean.NaturePic;

import java.util.List;

public interface IAboutView {
    public void setXRecyclerView(List<NaturePic> datas);
    public Activity getTheActivity();
    public void notifyData();
}
