package com.example.purescene.view.scene;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.purescene.R;
import com.example.purescene.presenter.CityPresenter;

import java.util.List;
import java.util.Objects;

public class CityFragment extends Fragment implements ICityView {

    /**
     * 控件、Presenter声明
     */
    private Button mBackButton;
    private TextView mCityTitleTextView;
    private ListView mCityListView;
    private ArrayAdapter<String> mArrayAdapter;
    private CityPresenter mCityPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_fragment, container, false);
        //初始化控件、Presenter
        mBackButton = view.findViewById(R.id.back_button);
        mCityTitleTextView = view.findViewById(R.id.city_title_text);
        mCityListView = view.findViewById(R.id.city_list);
        mCityPresenter = new CityPresenter(this);
        //初始化界面
        mCityPresenter.initView();
        return view;
    }

    /**
     * 第一次打开必定是省份信息，获取
     */
    public void queryFirstly(List<String> province) {
        //设置ListView
        mArrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, province);
        mCityListView.setAdapter(mArrayAdapter);
    }

    /**
     * 从市界面返回时根据已有的省信息更新ListView
     */
    public void queryProvince() {
        //在省界面设置按钮不可见不可用
        mBackButton.setVisibility(View.GONE);
        //设置上方TextView
        mCityTitleTextView.setText("全国");
        //更新ListView并将焦点移至首位
        mArrayAdapter.notifyDataSetChanged();
        mCityListView.setSelection(0);
    }

    /**
     * 获取城市信息并更新UI
     */
    public void queryCity(String cityTitle) {
        //设置Button可见且可用
        mBackButton.setVisibility(View.VISIBLE);
        //设置上方TextView为所选省份
        mCityTitleTextView.setText(cityTitle);
        //更新ListView并将焦点移至首位
        mArrayAdapter.notifyDataSetChanged();
        mCityListView.setSelection(0);
    }

    /**
     * 给ListView以及返回Button设置监听事件
     */
    public void setViewClickListener() {
        //设置ListView的监听事件
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCityPresenter.queryCityWithData(position);
            }
        });
        //设置返回Button的监听事件
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityPresenter.queryProvinceWithData();
            }
        });
    }

    /**
     * 用于在Presenter中获取Acitivity进行在主线程更新UI
     */
    public Activity getTheActivity(){
        return getActivity();
    }

}
