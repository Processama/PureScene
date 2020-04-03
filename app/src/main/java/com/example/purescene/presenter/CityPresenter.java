package com.example.purescene.presenter;


import android.util.Log;

import com.example.purescene.bean.City;
import com.example.purescene.bean.Province;
import com.example.purescene.bean.SpeCity;
import com.example.purescene.bean.SpeProvince;
import com.example.purescene.bean.UselessCity;
import com.example.purescene.bean.UselessProvince;
import com.example.purescene.model.DataModel;
import com.example.purescene.view.scene.ICityView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityPresenter {
    /**
     * Model层和View层声明
     */
    private DataModel mDataModel;
    private ICityView mCityView;
    /**
     * 分别是省数据、市数据、ListView的数据用于以及Gson
     */
    private List<SpeProvince> mSpeProvince;
    private List<SpeCity> mSpeCity;
    private List<String> mData;
    private Gson mGson;

    /**
     * 初始化Model和View
     */
    public CityPresenter(ICityView cityView) {
        mDataModel = new DataModel();
        mCityView = cityView;
        mSpeProvince = new ArrayList<>();
        mSpeCity = new ArrayList<>();
        mData = new ArrayList<>();
        mGson = new Gson();
    }

    /**
     * Presenter层初始化界面
     */
    public void initView() {
        queryProvinceWithData();
    }

    /**
     * 设置省数据,首先判断在第一次打开时没有省数据(mSpeProvince)，进行从网络上获取，并设置ListView设置监听更新UI，调用queryFirstly()；
     * 在从市数据界面打开时mSpeProvince以及有了直接清除mData重新设置数据并更新UI,调用queryProvince();
     */
    public void queryProvinceWithData() {
        if (mSpeProvince.size() == 0) {
            mDataModel.getProvince(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Province province = mGson.fromJson(Objects.requireNonNull(response.body()).string(), UselessProvince.class).getShowapi_res_body();
                    mSpeProvince = province.getList();
                    for (SpeProvince speProvince : mSpeProvince) {
                        mData.add(speProvince.getName());
                    }
                    mCityView.getTheActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCityView.queryFirstly(mData);
                            mCityView.setViewClickListener();
                        }
                    });
                }
            });
        } else {
            mData.clear();
            for (SpeProvince speProvince : mSpeProvince) {
                mData.add(speProvince.getName());
            }
            mCityView.queryProvince();;
        }
    }

    /**
     * 设置城市数据，当点击ListView子项时获取position，通过position获取SpeProvince的Proid用于获取数据，获取name用于设置TextView
     * 因为mData是省数据，清空，用获取的市数据更新即可调用queryCity，因为每次点击的可能是不同的城市，所以每次SpeCity数据都要重新从网络获取
     */
    public void queryCityWithData(final int position) {
        mDataModel.getCity(mSpeProvince.get(position).getId(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                mData.clear();
                City city = mGson.fromJson(Objects.requireNonNull(response.body()).string(), UselessCity.class).getShowapi_res_body();
                SpeCity tempSpeCity = new SpeCity();
                for (SpeCity speCity : city.getList()) {
                    if (speCity.getCityId() != null && !speCity.getCityId().equals(tempSpeCity.getCityId())) {
                        mSpeCity.add(speCity);
                        tempSpeCity = speCity;
                        mData.add(speCity.getCityName());
                    }
                }
                mCityView.getTheActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCityView.queryCity(mSpeProvince.get(position).getName());
                    }
                });
            }
        });
    }
}
