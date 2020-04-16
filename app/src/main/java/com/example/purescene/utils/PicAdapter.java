package com.example.purescene.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.purescene.R;
import com.example.purescene.bean.naturebean.NaturePic;

import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.PicViewHolder> {
    /**
     * 内部类ViewHolder,用于绑定子项布局
     */
    class PicViewHolder extends RecyclerView.ViewHolder {
        ImageView picImageView;

        public PicViewHolder(View view) {
            super(view);
            picImageView = view.findViewById(R.id.pic_image_view);
        }
    }

    /**
     * 声明数据
     */
    private List<NaturePic> mDatas;
    private Context mContext;

    public PicAdapter(List<NaturePic> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    @NonNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pic_item, viewGroup, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicViewHolder picViewHolder, int i) {
        Glide.with(mContext).load(mDatas.get(i).getBig()).into(picViewHolder.picImageView);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
