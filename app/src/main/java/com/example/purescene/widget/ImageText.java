package com.example.purescene.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
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
    private float mImageWidth;

    private float mImageHeight;
    private String mText;
    private float mTextSize;
    private int mTextColor;
    private boolean mClicked;

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
        mImageWidth = typedArray.getDimension(R.styleable.ImageText_image_width, 0);
        mImageHeight = typedArray.getDimension(R.styleable.ImageText_image_height, 0);
        mText = typedArray.getString(R.styleable.ImageText_text);
        mTextSize = typedArray.getDimension(R.styleable.ImageText_text_size, 0);
        mTextColor = typedArray.getColor(R.styleable.ImageText_text_color, 0);
        mClicked = typedArray.getBoolean(R.styleable.ImageText_clicked, false);
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
        setImageWidth(mImageWidth);
        setImageHeight(mImageHeight);
        setText(mText);
        setTextSize(mTextSize);
        setTextColor(mTextColor);
    }

    /**
     * 设置ImageView图片，可供外部使用
     */
    public void setImageRes(int imageRes) {
        mImageView.setImageResource(imageRes);
    }

    /**
     * 设置ImageView宽，可供外部使用
     */
    public void setImageWidth(float mImageWidth) {
            mImageView.getLayoutParams().width = (int)mImageWidth;
    }

    /**
     * 设置ImageView高，可供外部使用
     */
    public void setImageHeight(float mImageHeight) {
            mImageView.getLayoutParams().height = (int)mImageHeight;
    }

    /**
     * 设置TextView文本，可供外部使用
     */
    public void setText(String text) {
        mTextView.setText(text);
    }

    /**
     * 设置TextView字体大小，可供外部使用
     */
    public void setTextSize(float mTextSize) {
        //因为从xml文件sp数据获取时已经转为px，所以设置size用px单位，否则导致字体比实际偏大
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    /**
     * 设置TextView颜色，可供外部使用
     */
    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    /**
     * 设置是否被点击
     */
    public void setmClicked(boolean click) {
        mClicked = click;
    }

    /**
     * 获取点击状态
     */
    public boolean getmClicked() {
        return mClicked;
    }

}
