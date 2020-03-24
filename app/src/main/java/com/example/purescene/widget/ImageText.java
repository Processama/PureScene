package com.example.purescene.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.purescene.R;

public class ImageText extends LinearLayout {
    /**
     * 自定义组合按钮控件自定义属性声明
     */
    private ImageView mImageView;
    private TextView mTextView;
    private int mImageRes;
    private String mText;
    private int mTextColor;

    public ImageText(Context context) {
        this(context, null);
    }

    public ImageText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImageText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.image_text, this, true);
        mImageView = findViewById(R.id.ImageText_image_view);
        mTextView = findViewById(R.id.ImageText_text_view);

        obtainStyleAttrs(attrs);
        init();
    }

    /**
    * 获取自定义属性
    */
    private void obtainStyleAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageText);
        mImageRes = typedArray.getResourceId(R.styleable.ImageText_image, 0);
        mText = typedArray.getString(R.styleable.ImageText_text);
        mTextColor = typedArray.getColor(R.styleable.ImageText_text_color, 0);
        typedArray.recycle();
    }

    /**
     * 初始化
     */
    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setClickable(true);
        setFocusable(true);
        setGravity(Gravity.CENTER);

        //控件初始化
        setImageRes(mImageRes);
        setText(mText);
        setTextColor(mTextColor);
    }

    /**
     * 设置ImageView图片，可供外部使用
     */
    public void setImageRes(int imageRes) {
        mImageView.setImageResource(imageRes);
    }

    /**
     * 设置TextView文本，可供外部使用
     */
    public void setText(String text) {
        mTextView.setText(text);
    }

    /**
     * 设置TextView颜色，可供外部使用
     */
    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

}
