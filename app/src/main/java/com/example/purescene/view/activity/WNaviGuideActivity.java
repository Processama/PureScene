package com.example.purescene.view.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWNaviStatusListener;
import com.baidu.platform.comapi.walknavi.WalkNaviModeSwitchListener;
import com.baidu.platform.comapi.walknavi.widget.ArCameraView;
import com.example.purescene.R;

public class WNaviGuideActivity extends Activity {

    /**
     * 声明控件
     */
    WalkNavigateHelper mNaviHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wnavi_guide_activity);

        //获取WalkNavigateHelper实例
        mNaviHelper = WalkNavigateHelper.getInstance();
        //获取诱导页面地图展示View
        View view = mNaviHelper.onCreate(WNaviGuideActivity.this);
        if (view != null) {
            setContentView(view);
        }
        //AR导航
        mNaviHelper.setWalkNaviStatusListener(new IWNaviStatusListener() {
            /**
             * 普通步行导航模式和步行AR导航模式的切换
             * @param walkNaviModeSwitchListener 步行导航模式切换的监听器
             */
            @Override
            public void onWalkNaviModeChange(int mode, WalkNaviModeSwitchListener walkNaviModeSwitchListener) {
                mNaviHelper.switchWalkNaviMode(WNaviGuideActivity.this, mode, walkNaviModeSwitchListener);
            }

            @Override
            public void onNaviExit() {

            }
        });
        mNaviHelper.startWalkNavi(WNaviGuideActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ArCameraView.WALK_AR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaviHelper.startCameraAndSetMapView(WNaviGuideActivity.this);
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "需要开启相机使用权限", Toast.LENGTH_SHORT).show();
            }
        }
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
