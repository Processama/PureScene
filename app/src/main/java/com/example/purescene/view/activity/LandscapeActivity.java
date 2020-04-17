package com.example.purescene.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.purescene.R;
import com.example.purescene.bean.scenebean.SpeLandscape;
import com.example.purescene.widget.ImageText;

import java.util.Objects;

public class LandscapeActivity extends AppCompatActivity {

    /**
     * 控件声明
     */
    private TextView mLandscapeNameText;
    private TextView mLandscapeSummaryText;
    private TextView mLandscapeContentText;
    private TextView mLandscapeAddressText;
    private TextView mLandscapeAttentionText;
    private ImageView mLandscapeImage_1;
    private ImageView mLandscapeImage_2;
    private ImageView mLandscapeImage_3;

    private SpeLandscape mSpeLandscape;

    public LandscapeActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landscape_activity);

        //隐藏标题栏，设置状态栏文字颜色及图标为深色
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //初始哈
        mLandscapeNameText = findViewById(R.id.landscape_name_text);
        mLandscapeSummaryText = findViewById(R.id.landscape_summary_text);
        mLandscapeContentText = findViewById(R.id.landscape_content_text);
        mLandscapeAddressText = findViewById(R.id.landscape_address_text);
        mLandscapeAttentionText = findViewById(R.id.landscape_attention_text);
        mLandscapeImage_1 = findViewById(R.id.landscape_image_view_1);
        mLandscapeImage_2 = findViewById(R.id.landscape_image_view_2);
        mLandscapeImage_3 = findViewById(R.id.landscape_image_view_3);

        mSpeLandscape = getIntent().getParcelableExtra("landscape_data");
        ImageText searchRouteImageText = findViewById(R.id.search_route_image_text);
        searchRouteImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandscapeActivity.this, RouteSearchActivity.class);
                intent.putExtra("landscape_name", mSpeLandscape.getName());
                intent.putExtra("city_name", mSpeLandscape.getCityName());
                startActivity(intent);
            }
        });
        setView();
    }

    /**
     * 初始化界面
     */
    @SuppressLint("SetTextI18n")
    public void setView() {
        mLandscapeNameText.setText(mSpeLandscape.getName());
        if (mSpeLandscape.getSummary() == null) {
            mLandscapeSummaryText.setText("简介：暂无" );
        } else {
            mLandscapeSummaryText.setText("简介：" + mSpeLandscape.getSummary());
        }
        if (mSpeLandscape.getContent() == null) {
            mLandscapeContentText.setText("介绍：暂无");
        } else {
            mLandscapeContentText.setText("介绍：" + mSpeLandscape.getContent());
        }
        if (mSpeLandscape.getAddress() == null) {
            mLandscapeAddressText.setText("地址：" + mSpeLandscape.getAreaName());
        } else {
            mLandscapeAddressText.setText("地址：" + mSpeLandscape.getAddress());
        }
        if (mSpeLandscape.getAttention() == null) {
            mLandscapeAttentionText.setText("注意事项：暂无");
        } else {
            mLandscapeAttentionText.setText("注意事项：" + mSpeLandscape.getAttention());
        }
        if (mSpeLandscape.getPicList().size() >= 3) {
            Glide.with(this).load(mSpeLandscape.getPicList().get(0).getPicUrl()).error(R.drawable.error).into(mLandscapeImage_1);
            Glide.with(this).load(mSpeLandscape.getPicList().get(1).getPicUrl()).error(R.drawable.error).into(mLandscapeImage_2);
            Glide.with(this).load(mSpeLandscape.getPicList().get(2).getPicUrl()).error(R.drawable.error).into(mLandscapeImage_3);
        } else {
            Glide.with(this).load(mSpeLandscape.getPicList().get(0).getPicUrl()).error(R.drawable.error).into(mLandscapeImage_1);
        }
    }
}
