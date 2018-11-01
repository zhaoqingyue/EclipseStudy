package com.zhaoqy.app.demo.camera.gallery.util;

import android.content.Context;
import android.graphics.Bitmap.Config;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhaoqy.app.demo.R;

public class ImageLoaderHelper 
{
	public static ImageLoader mImageLoader = ImageLoader.getInstance(); //图片下载管理
	
	/**
	 * 
	 * @Title: initImageLoader
	 * @Description: 初始化图片下载器
	 * @param context
	 * @return: void
	 */
	public static void initImageLoader(Context context) 
	{
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCacheSize(10*1024*1024)
		.memoryCacheExtraOptions(96, 120)
		.build();
		mImageLoader.init(config);
	}
	
	/**
	 * 
	 * @Title: unInit
	 * @Description: 反初始化图片下载服务,清除缓存
	 * @return: void
	 */
	public static void unInitImageLoader()
	{
		if(mImageLoader != null)
		{
			mImageLoader.clearMemoryCache();
			//mImageLoader.clearDiscCache();
		}	
	}
	
	public static DisplayImageOptions mGalleryOption = new DisplayImageOptions.Builder()
													.showStubImage(R.drawable.custom_gallery_item_default_icon)        //设置图片下载期间显示的图片
													.showImageForEmptyUri(R.drawable.custom_gallery_item_default_icon) //设置图片Uri为空或是错误的时候显示的图片
													.showImageOnFail(R.drawable.custom_gallery_item_default_icon)      //设置图片加载或解码过程中发生错误显示的图片
													.cacheInMemory(true) //设置下载的图片是否缓存在内存中
													.cacheOnDisc(true)   //设置下载的图片是否缓存在SD卡中
													//.displayer(new RoundedBitmapDisplayer(20)) //设置成圆角图片
													.bitmapConfig(Config.RGB_565)
													.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
													.build(); //创建配置过的DisplayImageOption对象
	
	public static DisplayImageOptions mGalleryItemOption = new DisplayImageOptions.Builder()
													.showStubImage(R.drawable.custom_gallery_item_default_icon)        //设置图片下载期间显示的图片
													.showImageForEmptyUri(R.drawable.custom_gallery_item_default_icon) //设置图片Uri为空或是错误的时候显示的图片
													.showImageOnFail(R.drawable.custom_gallery_item_default_icon)      //设置图片加载或解码过程中发生错误显示的图片
													.cacheInMemory(true) //设置下载的图片是否缓存在内存中
													.cacheOnDisc(true)   //设置下载的图片是否缓存在SD卡中
													//.displayer(new RoundedBitmapDisplayer(20)) //设置成圆角图片
													.bitmapConfig(Config.ARGB_8888)
													.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
													.build(); //创建配置过的DisplayImageOption对象
}
