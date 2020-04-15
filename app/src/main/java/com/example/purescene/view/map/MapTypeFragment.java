package com.example.purescene.view.map;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.purescene.R;
import com.example.purescene.view.activity.ContentActivity;
import com.example.purescene.view.activity.RouteSearchActivity;
import com.example.purescene.widget.ImageText;

public class MapTypeFragment extends Fragment implements View.OnClickListener {

    /**
     * 声明控件
     */
    private ImageText mNormalImageText;
    private ImageText mSatelliteImageText;
    private ImageText mSituationImageText;
    private ImageText mTemperatureImageText;

    private int mBlack;
    private int mShineBlack;

    private IMapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_type_fragment, container, false);
        //初始化，设置点击事件
        mNormalImageText = view.findViewById(R.id.normal_map_image_text);
        mNormalImageText.setOnClickListener(this);
        mSatelliteImageText = view.findViewById(R.id.satellite_map_image_text);
        mSatelliteImageText.setOnClickListener(this);
        mSituationImageText = view.findViewById(R.id.situation_image_text);
        mSituationImageText.setOnClickListener(this);
        mTemperatureImageText = view.findViewById(R.id.temperature_image_text);
        mTemperatureImageText.setOnClickListener(this);

        mBlack = getResources().getColor(R.color.colorBlack,null);
        mShineBlack = getResources().getColor(R.color.colorShineBlack,null);

        if (getActivity() instanceof ContentActivity) {
            ContentActivity contentActivity = (ContentActivity) getActivity();
            mMapView = contentActivity.getmMapFragment();
        } else if (getActivity() instanceof RouteSearchActivity) {
            RouteSearchActivity routeSearchActivity = (RouteSearchActivity) getActivity();
            mMapView = routeSearchActivity.getmMapFragment();
        }

        return view;
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_map_image_text:
                changeMapType(mNormalImageText, mSatelliteImageText, R.drawable.normalmap, R.drawable.satelmap);
                mMapView.setNormalMap();
                break;
            case R.id.satellite_map_image_text:
                changeMapType(mSatelliteImageText, mNormalImageText, R.drawable.satellitemap, R.drawable.normap);
                mMapView.setSalliteMap();
                break;
            case R.id.situation_image_text:
                changeMapSetting(mSituationImageText, R.drawable.situation, R.drawable.situ);
                if (mSituationImageText.getmClicked()) {
                    mMapView.setSituation(true);
                } else {
                    mMapView.setSituation(false);
                }
                break;
            case R.id.temperature_image_text:
                changeMapSetting(mTemperatureImageText, R.drawable.temeprature, R.drawable.teme);
                if (mTemperatureImageText.getmClicked()) {
                    mMapView.setTemeprature(true);
                } else {
                    mMapView.setTemeprature(false);
                }
                break;
        }
    }

    /**
     * 改变样式
     */
    public void changeMapType(ImageText clicked, ImageText nothing, int clickedImage, int nothingImage) {
        if (!clicked.getmClicked()) {
            clicked.setmClicked(true);
            clicked.setImageRes(clickedImage);
            clicked.setTextColor(mBlack);
            nothing.setmClicked(false);
            nothing.setImageRes(nothingImage);
            nothing.setTextColor(mShineBlack);
        }
    }

    /**
     * 改变设置
     */
    public void changeMapSetting(ImageText imageText, int clickedImage, int nothingImage) {
        if (imageText.getmClicked()) {
            imageText.setmClicked(false);
            imageText.setImageRes(nothingImage);
            imageText.setTextColor(mShineBlack);
        } else {
            imageText.setmClicked(true);
            imageText.setImageRes(clickedImage);
            imageText.setTextColor(mBlack);
        }
    }
}
