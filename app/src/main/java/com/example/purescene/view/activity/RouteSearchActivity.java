package com.example.purescene.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purescene.R;
import com.example.purescene.view.map.IMapView;
import com.example.purescene.view.map.MapFragment;
import com.example.purescene.widget.ImageText;

import java.util.Objects;



public class RouteSearchActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 初始化控件
     */
    private IMapView mMapView;
    private Fragment mMapFragment;
    private EditText mStartEditText;
    private EditText mEndEidtText;
    private ImageText mWalkingImageText;
    private ImageText mBikingImageText;
    private ImageText mDrivingImageText;
    private FrameLayout mNaviFrameLayout;
    private ImageText mNaviImageText;

    private int mWay;
    private int mBlack;
    private int mShineBlack;

    public MapFragment getmMapFragment() {
        return (MapFragment) mMapFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_search_activity);
        //隐藏标题栏，设置状态栏文字颜色及图标为深色
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //初始化控件
        mMapFragment = new MapFragment();
        initFragment();

        mStartEditText = findViewById(R.id.start_edit_text);
        mEndEidtText = findViewById(R.id.end_edit_text);
        TextView mCancelText = findViewById(R.id.cancel_text);
        mCancelText.setOnClickListener(this);
        mWalkingImageText = findViewById(R.id.walking_image_text);
        mWalkingImageText.setOnClickListener(this);
        mBikingImageText = findViewById(R.id.biking_image_text);
        mBikingImageText.setOnClickListener(this);
        mDrivingImageText = findViewById(R.id.driving_image_text);
        mDrivingImageText.setOnClickListener(this);
        mNaviFrameLayout = findViewById(R.id.navi_framelayout);
        mNaviImageText = findViewById(R.id.navi_image_text);
        mNaviImageText.setOnClickListener(this);

        mWay = MapFragment.WALKING_SEARCH;
        mBlack = getResources().getColor(R.color.colorBlack,null);
        mShineBlack = getResources().getColor(R.color.colorShineBlack,null);

        searchRoute();

        //从景点界面跳转获取终点信息
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("city_name");
        String landscapeNmae = intent.getStringExtra("landscape_name");
        if (cityName != null && landscapeNmae != null) {
            String endText = cityName + " " + landscapeNmae;
            mEndEidtText.setText(endText);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView = (MapFragment) mMapFragment;
        //在onCreate中动态添加了Fragment但是还没有调用onCreateView会导致出现空指针异常
        mMapView.setSearchLayoutGone();
    }

    /**
     * 动态添加MapFragment，不使用静态的原因为出现了难以理解的bug
     */
    public void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.route_map_framelayout,mMapFragment);
        fragmentTransaction.commit();
    }

    /**
     * 各控件实现点击事件：取消、切换路线方式、导航
     */
    @Override
    public void onClick(View v) {
        String start = mStartEditText.getText().toString();
        String end = mEndEidtText.getText().toString();
        switch (v.getId()) {
            case R.id.cancel_text:
                finish();
                break;
            case R.id.walking_image_text:
                resetImgTextColorAndClick(mWalkingImageText);
                mWay = MapFragment.WALKING_SEARCH;
                if (!start.equals("") && !end.equals("")) {
                    mMapView.routeSearch(start, end, mWay);
                }
                break;
            case R.id.biking_image_text:
                resetImgTextColorAndClick(mBikingImageText);
                mWay = MapFragment.BIKING_SEARCH;
                if (!start.equals("") && !end.equals("")) {
                    mMapView.routeSearch(start, end, mWay);
                }
                break;
            case R.id.driving_image_text:
                resetImgTextColorAndClick(mDrivingImageText);
                mWay = MapFragment.DRIVING_SEARCH;
                if (!start.equals("") && !end.equals("")) {
                    mMapView.routeSearch(start, end, mWay);
                }
                break;
            case R.id.navi_image_text:
                mMapView.getSpotInfo(end, mWay);
                break;
        }
    }

    /**
     * 切换文字颜色
     */
    public void resetImgTextColorAndClick(ImageText imageText){
        mWalkingImageText.setTextColor(mShineBlack);
        mBikingImageText.setTextColor(mShineBlack);
        mDrivingImageText.setTextColor(mShineBlack);
        imageText.setTextColor(mBlack);
        if (!imageText.getmClicked()) {
            imageText.setmClicked(true);
        }
    }

    /**
     * 具体点击终点EditText后开始画路线
     */
    public void searchRoute() {
        mEndEidtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("ShowToast")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏软键盘
                    hideKeyboard(mEndEidtText);
                    String start = mStartEditText.getText().toString();
                    String end = mEndEidtText.getText().toString();
                    //判断搜索内容是否为空
                    if (start.equals("") || end.equals("")) {
                        Toast.makeText(RouteSearchActivity.this, "起点以及终点内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        mMapView.routeSearch(start, end, mWay);
                        mNaviFrameLayout.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
