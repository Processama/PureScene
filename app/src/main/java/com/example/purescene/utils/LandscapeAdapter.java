package com.example.purescene.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.purescene.R;
import com.example.purescene.bean.scenebean.SpeLandscape;

import java.util.List;

public class LandscapeAdapter extends RecyclerView.Adapter<LandscapeAdapter.LandscapeViewHolder> {
    /**
     * 内部类ViewHolder,用于绑定子项布局
     */
    class LandscapeViewHolder extends RecyclerView.ViewHolder {
        TextView landscapeNameText;
        TextView landscapeSummaryText;
        ImageView landscapeImage;

        public LandscapeViewHolder(View view) {
            super(view);
            landscapeNameText = view.findViewById(R.id.landscape_name_text);
            landscapeSummaryText = view.findViewById(R.id.landscape_summary_text);
            landscapeImage = view.findViewById(R.id.landscape_image_view);
        }
    }

    /**
     * 声明数据
     */
    private List<SpeLandscape> mDatas;
    private Context mContext;

    public LandscapeAdapter(List<SpeLandscape> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    /**
     * 解析子项布局，每滑到相应某一项生成ViewHolder，并加载子项布局
     */
    @NonNull
    @Override
    public LandscapeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.landscape_item, viewGroup, false);
        LandscapeViewHolder landscapeViewHolder = new LandscapeViewHolder(view);
        return landscapeViewHolder;
    }

    /**
     * 给上个方法传递过来的ViewHolder的子项布局的各个组件赋值
     */
    @Override
    public void onBindViewHolder(@NonNull LandscapeViewHolder landscapeViewHolder, int i) {
        SpeLandscape speLandscape = mDatas.get(i);
        landscapeViewHolder.landscapeNameText.setText(speLandscape.getName());
        landscapeViewHolder.landscapeSummaryText.setText(speLandscape.getSummary());
        Glide.with(mContext).load(speLandscape.getPicList().get(0).getPicUrl()).error(R.drawable.error).into(landscapeViewHolder.landscapeImage);
        //设置点击事件
        landscapeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
