package com.example.purescene.presenter;

import android.util.Log;

import com.example.purescene.bean.naturebean.Nature;
import com.example.purescene.bean.naturebean.NaturePic;
import com.example.purescene.bean.naturebean.SpeNature;
import com.example.purescene.bean.naturebean.UselessNature;
import com.example.purescene.model.DataModel;
import com.example.purescene.view.about.AboutFragment;
import com.example.purescene.view.about.IAboutView;
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

public class AboutPresenter {
    /**
     * Model层和View层声明
     */
    private DataModel mDataModel;
    private IAboutView mAboutView;

    /**
     * 声明数据
     */
    private List<NaturePic> mData;
    private Gson mGson;

    public AboutPresenter(IAboutView aboutView) {
        mDataModel = new DataModel();
        mAboutView = aboutView;
        mData = new ArrayList<>();
        mGson = new Gson();
    }

    public void setXRecyclerViewWithData(int page, final int state) {
        mDataModel.getPic(page, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Nature nature = mGson.fromJson(Objects.requireNonNull(response.body()).string(), UselessNature.class).getShowapi_res_body().getPagebean();
                for (SpeNature speNature : nature.getContentlist()) {
                    mData.addAll(speNature.getList());
                }
                mAboutView.getTheActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (state) {
                            case AboutFragment.INIT_STATE:
                                mAboutView.setXRecyclerView(mData);
                                break;
                            case AboutFragment.REFRESH_LOAD_STATE:
                                mAboutView.notifyData();
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
    public void loadData(int state) {
        AboutFragment.PIC_PAGE++;
        setXRecyclerViewWithData(AboutFragment.PIC_PAGE, AboutFragment.REFRESH_LOAD_STATE);
    }
}
