package com.example.purescene.view.map;

import android.Manifest;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.purescene.R;
import com.example.purescene.utils.overlayutil.BikingRouteOverlay;
import com.example.purescene.utils.overlayutil.DrivingRouteOverlay;
import com.example.purescene.utils.overlayutil.TransitRouteOverlay;
import com.example.purescene.utils.overlayutil.WalkingRouteOverlay;
import com.example.purescene.view.activity.RouteSearchActivity;
import com.example.purescene.widget.ImageText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements IMapView, View.OnClickListener {
    /**
     * 三种路线方式
     */
    public final static int WALKING_SEARCH = 1;
    public final static int BIKING_SEARCH = 2;
    public final static int DRIVING_SEARCH = 3;

    public static boolean flag = true;

    /**
     * 声明控件
     */
    private DrawerLayout mMapDrawerLayout;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private EditText mSearchRouteEditText;
    private LinearLayout mSearchRouteLinearLayout;

    /**
     * 定位
     */
    private LocationClient mLocationClient;
    private LatLng mLanlng;

    /**
     * 路线规划
     */
    private RoutePlanSearch mRoutePlanSearch;
    private WalkingRouteOverlay mWalkingRouteOverlay;
    private BikingRouteOverlay mBikingRouteOverlay;
    private DrivingRouteOverlay mDrivingRouteOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flag = true;
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        //初始化控件
        mMapView = view.findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        mMapDrawerLayout = view.findViewById(R.id.map_drawerlayout);
        mSearchRouteEditText = view.findViewById(R.id.search_route_edit_text);
        mSearchRouteLinearLayout = view.findViewById(R.id.search_route_linearlayout);
        ImageText mapOptionImageText = view.findViewById(R.id.map_option_image_text);
        Button locationButton = view.findViewById(R.id.location_button);

        mWalkingRouteOverlay = new WalkingRouteOverlay(mBaiduMap);
        mBikingRouteOverlay = new BikingRouteOverlay(mBaiduMap);
        mDrivingRouteOverlay = new DrivingRouteOverlay(mBaiduMap);

        //设置点击事件
        mSearchRouteEditText.setOnClickListener(this);
        mapOptionImageText.setOnClickListener(this);
        locationButton.setOnClickListener(this);

        //初始化地图默认2d地图、显示路况
        setNormalMap();
        setSituation(true);

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //申请权限，并定位
        getPermission();

        //初始化路线规划
        searchSpot();
        return view;
    }

    /**
     * 各控件点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_route_edit_text:
                Intent intent = new Intent(getContext(), RouteSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.map_option_image_text:
                mMapDrawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.location_button:
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
                    //申请权限成功直接定位
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
        option.setScanSpan(5000);

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

            //第一次打开时直接定位，以定位点为中心，并设置层级为17即比例尺100米
            if (flag) {
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(mLanlng, 17.0f);
                mBaiduMap.setMapStatus(status);
                flag = false;
            }
        }
    }

    /**
     * 路线规划
     */
    public void searchSpot() {
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                mBikingRouteOverlay.removeFromMap();
                mDrivingRouteOverlay.removeFromMap();
                if (walkingRouteResult.getRouteLines() != null && walkingRouteResult.getRouteLines().size() > 0) {
                    //获取路径规划数据
                    mWalkingRouteOverlay.setData(walkingRouteResult.getRouteLines().get(0));
                    //在地图上绘制WalkingRouteOverlay
                    mWalkingRouteOverlay.addToMap();
                } else {
                    Toast.makeText(getContext(), "很抱歉，没有找到相关路线，请确认地点信息是否正确!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                mWalkingRouteOverlay.removeFromMap();
                mBikingRouteOverlay.removeFromMap();
                if (drivingRouteResult.getRouteLines() != null && drivingRouteResult.getRouteLines().size() > 0) {
                    //获取路径规划数据,(以返回的第一条路线为例）
                    //为DrivingRouteOverlay实例设置数据
                    mDrivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(0));
                    //在地图上绘制DrivingRouteOverlay
                    mDrivingRouteOverlay.addToMap();
                } else {
                    Toast.makeText(getContext(), "很抱歉，没有找到相关路线，请确认地点信息是否正确!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                mWalkingRouteOverlay.removeFromMap();
                mDrivingRouteOverlay.removeFromMap();
                if (bikingRouteResult.getRouteLines() != null && bikingRouteResult.getRouteLines().size() > 0) {
                    //获取路径规划数据,(以返回的第一条路线为例）
                    //为BikingRouteOverlay实例设置数据
                    mBikingRouteOverlay.setData(bikingRouteResult.getRouteLines().get(0));
                    //在地图上绘制BikingRouteOverlay
                    mBikingRouteOverlay.addToMap();
                } else {
                    Toast.makeText(getContext(), "很抱歉，没有找到相关路线，请确认地点信息是否正确!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        //设置路线规划检索监听器
        mRoutePlanSearch.setOnGetRoutePlanResultListener(listener);
    }

    /**
     * 具体在地图上画路线，通过另一个界面调用
     */
     public void routeSearch(String start, String end, int way) {
         //准备起终点信息
         PlanNode stNode;
         PlanNode enNode;
         if (start.equals("我的位置")) {
             stNode = PlanNode.withLocation(mLanlng);
         } else {
             stNode = PlanNode.withCityNameAndPlaceName("", start);
         }
         if (end.equals("我的位置")) {
             enNode = PlanNode.withLocation(mLanlng);
         } else {
             enNode = PlanNode.withCityNameAndPlaceName("", end);
         }
         switch (way) {
            case WALKING_SEARCH:
                //发起检索
                mRoutePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
                break;
            case BIKING_SEARCH:
                mRoutePlanSearch.bikingSearch((new BikingRoutePlanOption())
                        .from(stNode)
                        .to(enNode)
                        // ridingType  0 普通骑行，1 电动车骑行
                        // 默认普通骑行
                        .ridingType(0));
                break;
            case DRIVING_SEARCH:
                mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
                break;
        }
     }

    /**
     * 设置在路线Activity中搜索框的整个布局为GONE
     */
    public void setSearchLayoutGone() {
         mSearchRouteLinearLayout.setVisibility(View.GONE);
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
        mRoutePlanSearch.destroy();
    }

}
