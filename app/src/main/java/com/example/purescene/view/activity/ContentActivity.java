package com.example.purescene.view.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.purescene.R;
import com.example.purescene.view.about.AboutFragment;
import com.example.purescene.view.map.MapFragment;
import com.example.purescene.view.me.MeFragment;
import com.example.purescene.view.scene.SceneFragment;
import com.example.purescene.widget.ImageText;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Fragment声明
     */
    private Fragment mSceneFragment;
    private Fragment mMapFragment;
    private Fragment mAboutFragment;
    private Fragment mMeFragment;
    private FragmentManager mFragmentManager;

    /**
     * 自定义按钮声明及文字颜色
     */
    private ImageText mSceneImageText;
    private ImageText mMapImagetText;
    private ImageText mAboutImageText;
    private ImageText mMeImageText;
    private int shineBlack;
    private int Black;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        //隐藏标题栏，设置状态栏文字颜色及图标为深色
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Fragment初始化
        mSceneFragment = new SceneFragment();
        mMapFragment = new MapFragment();
        mAboutFragment = new AboutFragment();
        mMeFragment = new MeFragment();
        mFragmentManager = getSupportFragmentManager();

        //ImageText初始化
        mSceneImageText = findViewById(R.id.scene_image_text);
        mSceneImageText.setOnClickListener(this);
        mMapImagetText = findViewById(R.id.map_image_text);
        mMapImagetText.setOnClickListener(this);
        mAboutImageText = findViewById(R.id.about_image_text);
        mAboutImageText.setOnClickListener(this);
        mMeImageText = findViewById(R.id.me_image_text);
        mMeImageText.setOnClickListener(this);

        shineBlack = getResources().getColor(R.color.colorShineBlack,null);
        Black = getResources().getColor(R.color.colorBlack,null);

        //初始切换为SceneFragment
        changeFragment(mSceneFragment);
    }

    /**
     * 切换Fragment
     */
    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_framelayout,fragment);
        fragmentTransaction.commit();
    }

    /**
     * 切换文字颜色
     */
    public void resetImgTextColor(ImageText imageText){
        mSceneImageText.setTextColor(shineBlack);
        mMapImagetText.setTextColor(shineBlack);
        mAboutImageText.setTextColor(shineBlack);
        mMeImageText.setTextColor(shineBlack);
        imageText.setTextColor(Black);
    }

    /**
     * 点击下方自定义按钮切换fragment以及改变文字颜色
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scene_image_text:
                changeFragment(mSceneFragment);
                resetImgTextColor(mSceneImageText);
                break;
            case R.id.map_image_text:
                changeFragment(mMapFragment);
                resetImgTextColor(mMapImagetText);
                break;
            case R.id.about_image_text:
                changeFragment(mAboutFragment);
                resetImgTextColor(mAboutImageText);
                break;
            case R.id.me_image_text:
                changeFragment(mMeFragment);
                resetImgTextColor(mMeImageText);
                break;
        }
    }
}
