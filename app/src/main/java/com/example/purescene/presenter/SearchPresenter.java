package com.example.purescene.presenter;

import com.example.purescene.bean.scenebean.Landscape;
import com.example.purescene.bean.scenebean.SpeLandscape;
import com.example.purescene.bean.scenebean.UselessLandscape;
import com.example.purescene.model.DataModel;
import com.example.purescene.view.activity.ISearchView;
import com.example.purescene.view.activity.SearchActivity;
import com.example.purescene.view.scene.SceneFragment;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchPresenter {
    /**
     * Model层和View层声明
     */
    private DataModel mDataModel;
    private ISearchView mSearchView;

    /**
     * 声明数据
     */
    private List<SpeLandscape> mData;
    private Gson mGson;

    /**
     * 初始化Model和View
     */
    public SearchPresenter(ISearchView searchView) {
        mDataModel = new DataModel();
        mSearchView = searchView;
        mData = new ArrayList<>();
        mGson = new Gson();
    }

    /**
     * 通过Model数据初始化XRecyclerView
     */
    public void setXRecyclerViewWithData(String keyword, final int state) {
        mDataModel.getSearchLandScape(keyword, SearchActivity.LANDSCAPE_PAGE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Landscape landscape = mGson.fromJson(response.body().string(), UselessLandscape.class).getShowapi_res_body().getPagebean();
                for (SpeLandscape speLandscape : landscape.getContentlist()) {
                    if (speLandscape.getPicList().size() != 0) {
                        mData.add(speLandscape);
                    }
                }
                mSearchView.getTheActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (state) {
                            case SceneFragment.INIT_STATE:
                                mSearchView.setXrecyclerView(mData);
                                break;
                            case SceneFragment.REFRESH_LOAD_STATE:
                                mSearchView.notifyData();
                                break;
                        }
                    }
                });
            }
        });
    }

    /**
     * 加载更多景点时，只需添加新数据到mData这个list并更新即可
     */
    public void loadData(String keyword, int state) {
        SearchActivity.LANDSCAPE_PAGE++;
        setXRecyclerViewWithData(keyword, state);
    }

    /**
     * 再次搜索时，page归0，更新adapter
     */
    public void searchAgain(String keyword, int state) {
        SearchActivity.LANDSCAPE_PAGE = 0;
        mData.clear();
        setXRecyclerViewWithData(keyword, state);
    }
}
