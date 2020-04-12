package com.example.purescene.view.scene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.purescene.R;
import com.example.purescene.bean.scenebean.SpeLandscape;
import com.example.purescene.presenter.ScenePresenter;
import com.example.purescene.utils.GlideImageLoader;
import com.example.purescene.utils.LandscapeAdapter;
import com.example.purescene.view.activity.SearchActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SceneFragment extends Fragment implements ISceneView,  View.OnClickListener {
    /**
     * 两种状态，分别时第一次打开，刷新加载
     */
    public final static int INIT_STATE = 0;
    public final static int REFRESH_LOAD_STATE = 1;

    private View mHeadView;
    private Banner mBanner;
    private TextView mCityNameTextView;
    private DrawerLayout mDrawerLayout;
    private XRecyclerView mLandscapeXrecyclerView;
    private LandscapeAdapter mLandscapeAdapter;
    private ScenePresenter mScenePresenter;

    /**
     * 声明cityId，cityName，从Activity传过来
     */
    private String mCityId;
    private String mCityName;
    public static int LANDSCAPE_PAGE = 1;

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scene_fragment, container, false);
        //初始化控件，解析头布局，将banner加进来
        mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.banner_layout,null,false);
        mBanner = mHeadView.findViewById(R.id.ad_banner);
        /**
         * 声明scene界面控件、presenter
         */
        Button mNavButton = view.findViewById(R.id.city_button);
        Button mSearchButton = view.findViewById(R.id.search_button);
        mCityNameTextView = view.findViewById(R.id.city_text);
        mDrawerLayout = view.findViewById(R.id.scene_drawerlayout);
        mLandscapeXrecyclerView = view.findViewById(R.id.landscape_xrecyclerview);
        mScenePresenter = new ScenePresenter(this);

        //设置按钮点击事件用于切换城市、搜索
        mSearchButton.setOnClickListener(this);
        mNavButton.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCityId = bundle.getString("cityId");
            mCityName = bundle.getString("cityName");
        }
        mScenePresenter.initView(mCityId);
        return view;
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_button:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.search_button:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
        }
    }

    /**
     * View层设置Banner,具体设置banner样式
     */
    public void setBanner(List<String> images) {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置banner图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片
        mBanner.setImages(images);
        //设置动画
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    /**
     * 通过presenter层获取景点数据，设置XRecyclerView
     */
    public void setViewFirstly(List<SpeLandscape> speLandscapes) {
        //设置TextView
        mCityNameTextView.setText(mCityName);
        //设置headView，包含banner
        mLandscapeXrecyclerView.addHeaderView(mHeadView);
        //设置LayoutManager和Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLandscapeAdapter = new LandscapeAdapter(speLandscapes, getContext());
        mLandscapeXrecyclerView.setLayoutManager(linearLayoutManager);
        mLandscapeXrecyclerView.setAdapter(mLandscapeAdapter);
        //设置监听事件进行刷新加载
        setRefreshAndLoad();
    }

    /**
     * 设置XRecyclerView的刷新加载功能
     */
    public void setRefreshAndLoad() {
        mLandscapeXrecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //模拟延时动画，取消图标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScenePresenter.refreshData(mCityId);
                        mLandscapeXrecyclerView.refreshComplete();
                    }
                },1000);
            }

            @Override
            public void onLoadMore() {
                //模拟延时动画，取消图标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScenePresenter.loadData(mCityId);
                        mLandscapeXrecyclerView.loadMoreComplete();
                    }
                },1000);
            }
        });
    }

    /**
     * 更新数据
     */
    public void notifyData() {
        mLandscapeAdapter.notifyDataSetChanged();
    }

    /**
     * 切换城市
     */
    public void changeCity(String cityId, String cityName) {
        //更新cityId，cityName
        mCityId = cityId;
        mCityName = cityName;
        //设置TextView
        mCityNameTextView.setText(mCityName);
        mScenePresenter.refreshData(mCityId);
    }

    /**
     * 用于在Presenter中获取Acitivity进行在主线程更新UI
     */
    public Activity getTheActivity(){
        return getActivity();
    }

}
