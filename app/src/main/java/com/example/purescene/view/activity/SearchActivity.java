package com.example.purescene.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purescene.R;
import com.example.purescene.bean.scenebean.SpeLandscape;
import com.example.purescene.presenter.SearchPresenter;
import com.example.purescene.utils.LandscapeAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements ISearchView {

    /**
     * 两种状态，分别时第一次打开，刷新加载
     */
    public final static int INIT_STATE = 0;
    public final static int REFRESH_LOAD_STATE = 1;
    public static int LANDSCAPE_PAGE = 1;
    public static int SEARCH_TIMES = 0;

    /**
     * 声明控件
     */
    private EditText mSearchEdit;
    private TextView mCancelText;
    private XRecyclerView mLandscapeXRecyclerView;
    private LandscapeAdapter mLandscapeAdapter;
    private SearchPresenter mSearchPresenter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        //隐藏标题栏，设置状态栏文字颜色及图标为深色
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //初始化控件
        mSearchEdit = findViewById(R.id.search_edit_text);
        mCancelText = findViewById(R.id.cancel_text);
        mLandscapeXRecyclerView = findViewById(R.id.landscape_xrecyclerview);
        mSearchPresenter = new SearchPresenter(this);

        setClick();
    }

    /**
     * 设置点击事件,取消text直接销毁该activity，搜索editText调用presenter层方法获取数据
     */
    public void setClick() {
        mCancelText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("ShowToast")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏软键盘
                    hideKeyboard(mSearchEdit);
                    String keyword = mSearchEdit.getText().toString();
                    //判断搜索内容是否为空
                    if (keyword.equals("")) {
                        Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        //判读是否是第一次成功搜索，因为第二次需要清空mData，页数归0再更新adapter即可；第一次设置XRecyclerView
                        SEARCH_TIMES++;
                        if (SEARCH_TIMES > 1) {
                            mSearchPresenter.searchAgain(keyword, REFRESH_LOAD_STATE);
                        } else {
                            mSearchPresenter.setXRecyclerViewWithData(keyword, INIT_STATE);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 设置XrecyclerView
     */
    public void setXrecyclerView(List<SpeLandscape> speLandscapes) {
        if (speLandscapes.size() == 0) {
            Toast.makeText(SearchActivity.this, "没有查找到相关内容", Toast.LENGTH_SHORT).show();
        }
        //设置LayoutManager和Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLandscapeAdapter = new LandscapeAdapter(speLandscapes, this);
        mLandscapeXRecyclerView.setLayoutManager(linearLayoutManager);
        mLandscapeXRecyclerView.setAdapter(mLandscapeAdapter);
        //设置刷新加载
        setRefreshAndLoad();
    }

    /**
     * 设置XRecyclerView的刷新加载功能
     */
    public void setRefreshAndLoad() {
        mLandscapeXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //模拟延时动画，取消图标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLandscapeXRecyclerView.refreshComplete();
                    }
                },500);
            }

            @Override
            public void onLoadMore() {
                //模拟延时动画，取消图标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSearchPresenter.loadData(mSearchEdit.getText().toString(), REFRESH_LOAD_STATE);
                        mLandscapeXRecyclerView.loadMoreComplete();
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

    public Activity getTheActivity() {
        return this;
    }

}
