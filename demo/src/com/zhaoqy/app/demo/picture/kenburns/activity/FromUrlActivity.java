package com.zhaoqy.app.demo.picture.kenburns.activity;

import java.io.File;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.kenburns.view.KenBurnsView;

public class FromUrlActivity extends Activity implements OnClickListener, ImageLoadingListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private KenBurnsView mImg;
    private ProgressBar mProgressBar;
    private ImageLoaderConfiguration config;
    private File cacheDir;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_kenburns_url);
		
		initView();
		setListener();
		initData();
		loadImage();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		mImg.resume();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		mImg = (KenBurnsView) findViewById(R.id.id_picture_kenburns_url);
        mProgressBar = (ProgressBar) findViewById(android.R.id.progress);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_kenburns_item2);
	}
	
	private void loadImage() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String cacheDirName = "." + getString(R.string.app_name);
            cacheDir = new File(Environment.getExternalStorageDirectory(), cacheDirName);
        } else {
            cacheDir = getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(5)
                .build();

        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .build();

        imageLoader.init(config);

        imageLoader.displayImage("http://i.imgur.com/gysR4Ee.jpg", mImg, options, this);
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) 
	{
	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) 
	{
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) 
	{
		mProgressBar.setVisibility(View.GONE);
        mImg.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) 
	{
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		mImg.pause();
	}
}
