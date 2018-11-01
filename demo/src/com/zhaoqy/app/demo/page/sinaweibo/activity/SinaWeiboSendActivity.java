package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.zhaoqy.app.demo.R;

public class SinaWeiboSendActivity extends Activity 
{
	public static final int GET_PIC = 101;
	private RelativeLayout mCenter;
	private Button    mCancle;
	private GridView  mGridView;
	private ImageView mImageView;
	private String    mFilepath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_send);
		
		initview();
		setListener();
	}

	private void initview() 
	{
		mCancle = (Button) findViewById(R.id.id_sinaweibo_send_cancle);
		mImageView = (ImageView) findViewById(R.id.id_sinaweibo_send_imgview);
		mCenter = (RelativeLayout) findViewById(R.id.id_sinaweibo_send_center);
		mGridView = (GridView) findViewById(R.id.id_sinaweibo_send_emotions);
	}

	private void setListener() 
	{
		mCancle.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
	}

	public void doclick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_sinaweibo_send_camera:
		{
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent, GET_PIC);
			break;
		}
		case R.id.id_sinaweibo_send_photo:
		{
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, GET_PIC);
			break;
		}
		case R.id.id_sinaweibo_send_mention:
			break;
		case R.id.id_sinaweibo_send_emotion:
		{ 
			mCenter.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.VISIBLE);
			break;
		}
		case R.id.id_sinaweibo_send_more:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK) 
		{
			if (requestCode == GET_PIC) 
			{
				Uri uri = data.getData();
				if (uri == null)
					return;
				Cursor c = getContentResolver().query(uri, new String[] { Media.DATA }, null, null, null);
				if (c != null) 
				{
					c.moveToFirst();
					mFilepath = c.getString(c.getColumnIndex(Media.DATA));
					c.close();
					// 设置一个缩放
					Options options = new Options();
					options.inSampleSize = 4;
					mImageView.setImageBitmap(BitmapFactory.decodeFile(mFilepath, options));
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
