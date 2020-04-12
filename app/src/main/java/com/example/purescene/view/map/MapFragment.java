package com.example.purescene.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.purescene.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements IMapView, View.OnClickListener {
    public static boolean flag = true;

    /**
     * 声明控件
     */
    private DrawerLayout mMapDrawerLayout;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private LocationClient mLocationClient;
    private LatLng mLanlng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        //初始化控件
        flag = true;
        mMapView = view.findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        mMapDrawerLayout = view.findViewById(R.id.map_drawerlayout);
        Button mapOptionButton = view.findViewById(R.id.map_option_button);
        Button locationButton = view.findViewById(R.id.location_button);

        //设置点击事件
        mapOptionButton.setOnClickListener(this);
        locationButton.setOnClickListener(this);

        //初始化地图默认2d地图、显示路况
        setNormalMap();
        setSituation(true);

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //申请权限，并定位
        getPermission();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_option_button:
                mMapDrawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.location_button:
                Log.d("fdsfdsfds", mLanlng.latitude+"");
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(mLanlng, 17.0f);
                mBaiduMap.setMapStatus(status);
                break;
        }
    }

    /**
     * 动态申请权限
     */
    public void getPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            requestPermissions(permissions, 1);
        } else {
            setLocation();
        }
    }

    /**
     * 申请权限结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "拒绝授权权限，某些功能可能会无法运行如定位功能", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    setLocation();
                } else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 设置地图类型，2d图或卫星图
     */
    public void setNormalMap() {
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    public void setSalliteMap() {
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
    }

    /**
     * 设置地图路况、热力图
     */
    public void setSituation(boolean situation) {
        mBaiduMap.setTrafficEnabled(situation);
    }

    public void setTemeprature(boolean temperature) {
        mBaiduMap.setBaiduHeatMapEnabled(temperature);
    }

    /**
     * 发起定位
     */
    public void setLocation() {
        //定位初始化
        mLocationClient = new LocationClient(getActivity());

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);  //打开gps
        option.setCoorType("bd09ll");  //设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        //开启地图定位图层
        mLocationClient.start();

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mLanlng = new LatLng(location.getLatitude(), location.getLongitude());

            /**
             * 第一次打开界面时以定位点为中心，并设置层级为17即比例尺100米
             */
            if (flag) {
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(mLanlng, 17.0f);
                mBaiduMap.setMapStatus(status);
                flag = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //fragment执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

}
