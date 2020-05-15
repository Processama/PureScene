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
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.purescene.R;
import com.example.purescene.utils.overlayutil.BikingRouteOverlay;
import com.example.purescene.utils.overlayutil.DrivingRouteOverlay;
import com.example.purescene.utils.overlayutil.WalkingRouteOverlay;
import com.example.purescene.view.activity.BNaviGuideActivity;
import com.example.purescene.view.activity.RouteSearchActivity;
import com.example.purescene.view.activity.WNaviGuideActivity;
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
    private LatLng mLatLng;

    /**
     * 路线规划
     */
    private RoutePlanSearch mRoutePlanSearch;
    private WalkingRouteOverlay mWalkingRouteOverlay;
    private BikingRouteOverlay mBikingRouteOverlay;
    private DrivingRouteOverlay mDrivingRouteOverlay;

    /**
     * 地理编码
     */
    private GeoCoder mGeoCoder;
    private LatLng mTempLatLng;
    private int mNavigationWay;

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

        //初始化导航
        initBikeNavigation();
        initNavigation();

        //创建地理编码检索实例
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                    if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                        return;
                    } else {
                        double latitude = geoCodeResult.getLocation().latitude;
                        double longitude = geoCodeResult.getLocation().longitude;
                        mTempLatLng = new LatLng(latitude, longitude);
                        switch (mNavigationWay) {
                            case WALKING_SEARCH:
                                startNavigation(mLatLng, mTempLatLng);
                                break;
                            case BIKING_SEARCH:
                                startBikeNavigation(mLatLng, mTempLatLng);
                                break;
                            case DRIVING_SEARCH:
                                Toast.makeText(getContext(), "很抱歉，暂未开通骑行导航功能", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        });
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
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(mLatLng, 17.0f);
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
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            //第一次打开时直接定位，以定位点为中心，并设置层级为17即比例尺100米
            if (flag) {
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(mLatLng, 17.0f);
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
             stNode = PlanNode.withLocation(mLatLng);
         } else {
             String[] str = start.split("\\s+");
             if (str.length > 1) {
                 stNode = PlanNode.withCityNameAndPlaceName(str[0], str[1]);
             } else {
                 stNode = PlanNode.withCityNameAndPlaceName("", start);
             }
         }
         if (end.equals("我的位置")) {
             enNode = PlanNode.withLocation(mLatLng);
         } else {
             String[] ed = end.split("\\s+");
             if (ed.length > 1) {
                 enNode = PlanNode.withCityNameAndPlaceName(ed[0], ed[1]);
             } else {
                 enNode = PlanNode.withCityNameAndPlaceName("", end);
             }
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
     * 导航前获取经纬度,适配获取步行、骑行两种方式
     */
    public void getSpotInfo(String end, int way) {
        String[] ed = end.split("\\s+");
        if (ed.length > 1) {
            mNavigationWay = way;
            mGeoCoder.geocode(new GeoCodeOption()
                    .city(ed[0])
                    .address(ed[1]));
        } else {
            Toast.makeText(getContext(), "需要详细位置信息", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 初始化步行导航
     */
    public void initNavigation() {
        // 获取导航控制类
        // 引擎初始化
        WalkNavigateHelper.getInstance().initNaviEngine(getActivity(), new IWEngineInitListener() {

            @Override
            public void engineInitSuccess() {
                //引擎初始化成功的回调
            }

            @Override
            public void engineInitFail() {
                //引擎初始化失败的回调
            }
        });
    }

    /**
     * 发起导航，在另一个界面获取起终点值后调用
     */
    public void startNavigation(LatLng startPt, LatLng endPt) {
        //构造WalkNaviLaunchParam
        WalkNaviLaunchParam mParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);

        //发起算路
        WalkNavigateHelper.getInstance().routePlanWithParams(mParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                //开始算路的回调
            }

            @Override
            public void onRoutePlanSuccess() {
                //算路成功
                //跳转至诱导页面
                Intent intent = new Intent(getContext(), WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                //算路失败的回调
            }
        });
    }

    /**
     * 初始化骑行导航
     */
    public void initBikeNavigation() {
        // 获取导航控制类
        // 引擎初始化
        BikeNavigateHelper.getInstance().initNaviEngine(getActivity(), new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                //骑行导航初始化成功之后的回调
            }

            @Override
            public void engineInitFail() {
                //骑行导航初始化失败之后的回调
            }
        });
    }

    /**
     * 发起骑行导航
     */
    public void startBikeNavigation(LatLng startPt, LatLng endPt) {
        //构造BikeNaviLaunchParam
        //.vehicle(0)默认的普通骑行导航
        BikeNaviLaunchParam mParam = new BikeNaviLaunchParam().stPt(startPt).endPt(endPt).vehicle(0);

        //发起算路
        BikeNavigateHelper.getInstance().routePlanWithParams(mParam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                //执行算路开始的逻辑
            }

            @Override
            public void onRoutePlanSuccess() {
                //算路成功
                //跳转至诱导页面
                Intent intent = new Intent(getContext(), BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError bikeRoutePlanError) {
                //执行算路失败的逻辑
            }
        });
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
        mGeoCoder.destroy();
    }

}
