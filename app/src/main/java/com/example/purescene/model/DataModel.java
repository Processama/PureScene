package com.example.purescene.model;

import android.util.Log;

import com.example.purescene.utils.HttpUtil;

import okhttp3.Callback;

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

    /**
     * 获取景点数据
     */
    public void getLandScape(String cityId, int page, Callback callback) {
        String extraUrl;
        if (cityId.equals("3") || cityId.equals("25") || cityId.equals("27") || cityId.equals("32")) {
            extraUrl = "&proId=" + cityId;
        } else {
            extraUrl = "&cityId=" + cityId;
        }
        String url = "http://route.showapi.com/268-1?showapi_appid=168422&showapi_sign=2a4402689bae4699a772b7976e707fba&page=" + page + extraUrl;
        HttpUtil.sendOkHttpRequest(url, callback);
    }

    /**
     * 获取景点图片
     */
    public void getPic(int page, Callback callback) {
        String url = "http://route.showapi.com/852-2?showapi_appid=168422&showapi_sign=2a4402689bae4699a772b7976e707fba&type=6004&page=" + page;
        HttpUtil.sendOkHttpRequest(url, callback);
    }

}
