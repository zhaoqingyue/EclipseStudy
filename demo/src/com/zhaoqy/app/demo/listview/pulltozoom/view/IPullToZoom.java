package com.zhaoqy.app.demo.listview.pulltozoom.view;

import android.content.res.TypedArray;
import android.view.View;

public interface IPullToZoom<T extends View>
{
    public View getZoomView();

    public View getHeaderView();

    public T getPullRootView();

    public boolean isPullToZoomEnabled();

    public boolean isZooming();

    public boolean isParallax();

    public boolean isHideHeader();

    public void handleStyledAttributes(TypedArray a);
}
