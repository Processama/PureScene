package com.example.purescene.view;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.purescene.R;
import com.example.purescene.widget.ImageText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.main_activity);

        //隐藏标题栏，设置状态栏文字颜色及图标为深色
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
                resetImgTextColor(mSceneImageText);
                break;
            case R.id.map_image_text:
                resetImgTextColor(mMapImagetText);
                break;
            case R.id.about_image_text:
                resetImgTextColor(mAboutImageText);
                break;
            case R.id.me_image_text:
                resetImgTextColor(mMeImageText);
                break;
        }
    }
}
