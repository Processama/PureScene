package com.example.purescene.presenter;

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
    private Gson mGson;

    /**
     * 初始化Model和View
     */
    public ScenePresenter(ISceneView sceneView) {
        mDataModel = new DataModel();
        mSceneView = sceneView;
        mData = new ArrayList<>();
        mGson = new Gson();
    }

    /**
     * Presenter层初始化界面
     */
    public void initView(String cityId){
        setXRecyclerViewWithData(cityId, SceneFragment.INIT_STATE);
    }

    /**
     * 设置景点数据，通过cityId从Model层取出数据设置XRecyclerView
     */
    public void setXRecyclerViewWithData(String cityId, final int state) {
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
