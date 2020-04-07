package com.example.purescene.bean.scenebean;

import android.os.Parcel;
import android.os.Parcelable;

public class LandscapePic implements Parcelable {
    private String picUrl;
    private String picUrlSmall;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrlSmall() {
        return picUrlSmall;
    }

    public void setPicUrlSmall(String picUrlSmall) {
        this.picUrlSmall = picUrlSmall;
    }

    /**
     * 通过Parcelable方式序列化
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picUrl);
        dest.writeString(picUrlSmall);
    }

    public static final Parcelable.Creator<LandscapePic> CREATOR = new Parcelable.Creator<LandscapePic>() {

        @Override
        public LandscapePic createFromParcel(Parcel source) {
            LandscapePic landscapePic = new LandscapePic();
            landscapePic.picUrl = source.readString();
            landscapePic.picUrlSmall = source.readString();
            return landscapePic;
        }

        @Override
        public LandscapePic[] newArray(int size) {
            return new LandscapePic[size];
        }
    };
}
