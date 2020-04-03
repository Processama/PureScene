package com.example.purescene.model;

import com.example.purescene.bean.City;
import com.example.purescene.bean.Province;
import com.example.purescene.bean.SpeCity;
import com.example.purescene.bean.SpeProvince;
import com.example.purescene.utils.HttpUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DataModel {

    /**
     * 获取省数据
     */
    public void getProvince(Callback callback) {
        HttpUtil.sendOkHttpRequest("http://route.showapi.com/268-2?showapi_appid=168422&showapi_sign=2a4402689bae4699a772b7976e707fba", callback);
    }

    /**
     * 获取市数据
     */
    public void getCity(String id, Callback callback) {
        String url = "http://route.showapi.com/268-3?showapi_appid=168422&proId=" + id + "&showapi_sign=2a4402689bae4699a772b7976e707fba";
        HttpUtil.sendOkHttpRequest(url, callback);
    }
}
