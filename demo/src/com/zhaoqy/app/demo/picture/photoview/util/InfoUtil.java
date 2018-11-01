package com.zhaoqy.app.demo.picture.photoview.util;

import android.graphics.RectF;
import android.widget.ImageView;

public class InfoUtil 
{
	//内部图片在整个窗口的位置
    public RectF mRect = new RectF();
    //控件在窗口的位置
    RectF mLocalRect = new RectF();
    public RectF mImgRect = new RectF();
    public RectF mWidgetRect = new RectF();
    float mScale;
    public float mDegrees;
    public ImageView.ScaleType mScaleType;

    public InfoUtil(RectF rect, RectF local, RectF img, RectF widget, float scale, float degrees, ImageView.ScaleType scaleType) 
    {
        mRect.set(rect);
        mLocalRect.set(local);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
    }
}
