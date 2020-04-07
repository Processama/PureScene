package com.example.purescene.presenter;

import com.example.purescene.bean.naturebean.Nature;
import com.example.purescene.bean.naturebean.SpeNature;
import com.example.purescene.bean.naturebean.UselessNature;
import com.example.purescene.bean.scenebean.Landscape;
import com.example.purescene.bean.scenebean.SpeLandscape;
import com.example.purescene.bean.scenebean.UselessLandscape;
import com.example.purescene.model.DataModel;
import com.example.purescene.view.scene.ISceneView;
import com.example.purescene.view.scene.SceneFragment;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ScenePresenter {
    /**
     * Model层和View层声明
     */
    private DataModel mDataModel;
    private ISceneView mSceneView;

    /**
     * 声明数据
     */
    private List<SpeLandscape> mData;
    private List<String> mImages;
    private Gson mGson;
    private Random mRandom;

    /**
     * 初始化Model和View
     */
    public ScenePresenter(ISceneView sceneView) {
        mDataModel = new DataModel();
        mSceneView = sceneView;
        mData = new ArrayList<>();
        mImages = new ArrayList<>();
        mGson = new Gson();
        mRandom = new Random();
    }

    /**
     * Presenter层初始化界面
     */
    public void initView(String cityId){
        setBannerWithData();
        setXRecyclerViewWithData(cityId, SceneFragment.INIT_STATE);
    }

    /**
     * 设置图片给banner
     */
    private void setBannerWithData() {
        int page = mRandom.nextInt(25) + 1;
        mDataModel.getPic(page, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Nature nature = mGson.fromJson(Objects.requireNonNull(response.body()).string(), UselessNature.class).getShowapi_res_body().getPagebean();
                for (SpeNature speNature : nature.getContentlist()) {
                    mImages.add(speNature.getList().get(0).getBig());
                }
                mSceneView.getTheActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> images = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            images.add(mImages.get(mRandom.nextInt(mImages.size())));
                        }
                        mSceneView.setBanner(images);
                    }
                });
            }
        });
    }

    /**
     * 设置景点数据，通过cityId从Model层取出数据设置XRecyclerView
     */
    private void setXRecyclerViewWithData(String cityId, final int state) {
        mDataModel.getLandScape(cityId, SceneFragment.LANDSCAPE_PAGE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Landscape landscape = mGson.fromJson(Objects.requireNonNull(response.body()).string(), UselessLandscape.class).getShowapi_res_body().getPagebean();
                for (SpeLandscape speLandscape : landscape.getContentlist()) {
                    if (speLandscape.getPicList().size() != 0) {
                        mData.add(speLandscape);
                    }
                }
                mSceneView.getTheActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (state) {
                            case SceneFragment.INIT_STATE:
                                mSceneView.setViewFirstly(mData);
                                break;
                            case SceneFragment.REFRESH_LOAD_STATE:
                                mSceneView.notifyData();
                                break;
                        }
                    }
                });


            }
        });
    }

    /**
     * 刷新时，清空mData并再赋值一次即可
     */
    public void refreshData(String cityId) {
        SceneFragment.LANDSCAPE_PAGE = 1;
        mData.clear();
        setBannerWithData();
        setXRecyclerViewWithData(cityId, SceneFragment.REFRESH_LOAD_STATE);
    }

    /**
     * 加载更多景点时，只需添加新数据到mData这个list并更新即可
     */
    public void loadData(String cityId) {
        SceneFragment.LANDSCAPE_PAGE++;
        setXRecyclerViewWithData(cityId, SceneFragment.REFRESH_LOAD_STATE);
    }
}
