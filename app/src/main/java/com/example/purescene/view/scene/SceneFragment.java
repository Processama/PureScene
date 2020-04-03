package com.example.purescene.view.scene;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.purescene.R;

public class SceneFragment extends Fragment implements ISceneView {
    /**
     * 声明scene界面控件
     */
    private Button mButton;
    private TextView mTextView;
    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scene_fragment, container, false);
        //初始化控件
        mButton = view.findViewById(R.id.city_button);
        mTextView = view.findViewById(R.id.city_text);
        mDrawerLayout = view.findViewById(R.id.scene_drawerlayout);
        return view;
    }
}
