package com.example.purescene.view.scene;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.purescene.R;
import com.example.purescene.bean.scenebean.SpeLandscape;
import com.example.purescene.presenter.ScenePresenter;
import com.example.purescene.utils.LandscapeAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class SceneFragment extends Fragment implements ISceneView {
    /**
     * 三种状态，分别时第一次打开，刷新，加载
     */
    public final static int INIT_STATE = 0;
    public final static int REFRESH_LOAD_STATE = 1;

    /**
     * 声明scene界面控件、presenter
     */
    private Button mButton;
    private TextView mTextView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scene_fragment, container, false);
        //初始化控件
        mButton = view.findViewById(R.id.city_button);
        mTextView = view.findViewById(R.id.city_text);
        mDrawerLayout = view.findViewById(R.id.scene_drawerlayout);
        mLandscapeXrecyclerView = view.findViewById(R.id.landscape_xrecyclerview);
        mScenePresenter = new ScenePresenter(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCityId = bundle.getString("cityId");
            mCityName = bundle.getString("cityName");
        }
        mScenePresenter.initView(mCityId);
        return view;
    }

    /**
     * 通过presenter层获取景点数据，设置XRecyclerView
     */
    public void setViewFirstly(List<SpeLandscape> speLandscapes) {
        //设置TextView
        mTextView.setText(mCityName);
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

    public void notifyData() {
        mLandscapeAdapter.notifyDataSetChanged();
    }

    /**
     * 用于在Presenter中获取Acitivity进行在主线程更新UI
     */
    public Activity getTheActivity(){
        return getActivity();
    }
}
