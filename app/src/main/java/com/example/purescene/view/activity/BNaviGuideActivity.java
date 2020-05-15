package com.example.purescene.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.example.purescene.R;

public class BNaviGuideActivity extends Activity {
    /**
     * 声明控件
     */
    BikeNavigateHelper mNaviHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bnavi_guide_activity);

        //获取BikeNavigateHelper示例
        mNaviHelper = BikeNavigateHelper.getInstance();
        // 获取诱导页面地图展示View
        View view = mNaviHelper.onCreate(BNaviGuideActivity.this);

        if (view != null) {
            setContentView(view);
        }

        // 开始导航
        mNaviHelper.startBikeNavi(BNaviGuideActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }
}
