/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaImageActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 图片对话框
 * @author: zhaoqy
 * @date: 2015-11-23 上午10:17:13
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.common.ApiClient;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.util.FileUtils;
import com.zhaoqy.app.demo.oschina.util.ImageUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

@SuppressLint("HandlerLeak")
public class OSChinaImageActivity extends OSChinaBaseActivity implements OnClickListener
{
	private Context      mContext;
	private ViewSwitcher mViewSwitcher;
	private ImageButton  mClose;
	private ImageView    mImage;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_image);
        mContext = this;
        
        initView();
        setListener();
        initData();
    }
	private void initView()
    {
    	mViewSwitcher = (ViewSwitcher)findViewById(R.id.imagedialog_view_switcher); 
    	mClose = (ImageButton)findViewById(R.id.imagedialog_close_button);
        mImage = (ImageView)findViewById(R.id.imagedialog_image);
    } 
	
	private void setListener() 
    {
    	mClose.setOnClickListener(this);
	}
    
    private void initData() 
    {
		final String imgURL = getIntent().getStringExtra("img_url");		
		final String ErrMsg = "图片加载失败";
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what==1 && msg.obj != null)
				{
					mImage.setImageBitmap((Bitmap)msg.obj);
					mViewSwitcher.showNext();
				}
				else
				{
					UIHelper.ToastMessage(mContext, ErrMsg);
					finish();
				}
			}
		};
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				Bitmap bmp = null;
		    	String filename = FileUtils.getFileName(imgURL);
				try 
				{
					//读取本地图片
					if(imgURL.endsWith("portrait.gif") || StringUtils.isEmpty(imgURL))
					{
						bmp = BitmapFactory.decodeResource(mImage.getResources(), R.drawable.oschina_dface);
					}
					if(bmp == null)
					{
						//是否有缓存图片
				    	//Environment.getExternalStorageDirectory();返回/sdcard
				    	String filepath = getFilesDir() + File.separator + filename;
						File file = new File(filepath);
						if(file.exists())
						{
							bmp = ImageUtils.getBitmap(mImage.getContext(), filename);
							if(bmp != null)
							{
								//缩放图片
								bmp = ImageUtils.reDrawBitMap(OSChinaImageActivity.this, bmp);
							}
				    	}
					}
					if(bmp == null)
					{
						bmp = ApiClient.getNetBitmap(imgURL);
						if(bmp != null)
						{
							try 
							{
		                    	//写图片缓存
								ImageUtils.saveImage(mImage.getContext(), filename, bmp);
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
							}
							//缩放图片
							bmp = ImageUtils.reDrawBitMap(OSChinaImageActivity.this, bmp);
						}
					}
					msg.what = 1;
					msg.obj = bmp;
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.imagedialog_close_button:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
}
