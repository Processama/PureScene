package com.example.purescene.bean.scenebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SpeLandscape implements Parcelable {
    private String cityName;
    private String areaName;
    private String summary;
    private String address;
    private String name;
    private String content;
    private List<LandscapePic> picList;
    private String attention;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 通过Parcelable方式序列化
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(areaName);
        dest.writeString(summary);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeTypedList(picList);
        dest.writeString(attention);
    }

    public static final Parcelable.Creator<SpeLandscape> CREATOR = new Parcelable.Creator<SpeLandscape>() {

        @Override
        public SpeLandscape createFromParcel(Parcel source) {
            SpeLandscape speLandscape = new SpeLandscape();
            speLandscape.cityName = source.readString();
            speLandscape.areaName = source.readString();
            speLandscape.summary = source.readString();
            speLandscape.address = source.readString();
            speLandscape.name = source.readString();
            speLandscape.content = source.readString();
            speLandscape.picList = source.createTypedArrayList(LandscapePic.CREATOR);
            speLandscape.attention = source.readString();
            return speLandscape;
        }

        @Override
        public SpeLandscape[] newArray(int size) {
            return new SpeLandscape[size];
        }
    };
}
