package com.example.purescene.view.about;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.purescene.R;
import com.example.purescene.bean.naturebean.NaturePic;
import com.example.purescene.presenter.AboutPresenter;
import com.example.purescene.utils.PicAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class AboutFragment extends Fragment implements IAboutView {
    /**
     * 两种状态，分别时第一次打开，刷新加载
     */
    public final static int INIT_STATE = 0;
    public final static int REFRESH_LOAD_STATE = 1;
    public static int PIC_PAGE = 1;
    /**
     *  声明控件
     */
    private XRecyclerView mPicXRecyclerView;
    private PicAdapter mPicAdapter;
    private AboutPresenter mAboutPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        //初始化控件
        mPicXRecyclerView = view.findViewById(R.id.pic_xrecyclerview);

        mAboutPresenter = new AboutPresenter(this);
        mAboutPresenter.setXRecyclerViewWithData(PIC_PAGE, INIT_STATE);
        return view;
    }

    /**
     * 设置XRecyclerView
     */
    public void setXRecyclerView(List<NaturePic> datas) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPicAdapter = new PicAdapter(datas, getContext());
        mPicXRecyclerView.setLayoutManager(layoutManager);
        mPicXRecyclerView.setAdapter(mPicAdapter);

        setRefreshAndLoad();
    }

    /**
     * 设置XRecyclerView的刷新加载功能
     */
    public void setRefreshAndLoad() {
        mPicXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //模拟延时动画，取消图标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPicXRecyclerView.refreshComplete();
                    }
                },500);
            }

            @Override
            public void onLoadMore() {
                //模拟延时动画，取消图标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAboutPresenter.loadData(REFRESH_LOAD_STATE);
                        mPicXRecyclerView.loadMoreComplete();
                    }
                },1000);
            }
        });
    }

    /**
     * 更新数据
     */
    public void notifyData() {
        mPicAdapter.notifyDataSetChanged();
    }

    /**
     * 用于在presenter层中回到主线程
     */
    public Activity getTheActivity() {
        return getActivity();
    }
}
