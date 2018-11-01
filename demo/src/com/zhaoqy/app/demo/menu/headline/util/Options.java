package com.zhaoqy.app.demo.menu.headline.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class Options 
{
	public static DisplayImageOptions getListOptions() 
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565)
				//.considerExifParams(true)
				.resetViewBeforeLoading(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.build();
		return options;
	}
}
