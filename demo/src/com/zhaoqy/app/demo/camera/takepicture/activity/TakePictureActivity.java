package com.zhaoqy.app.demo.camera.takepicture.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.takepicture.util.FileUtil;

public class TakePictureActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mSystemPhoto;
	private TextView  mSystemVideo;
	private TextView  mAPIPhoto;
	private TextView  mAPIVideo;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSystemPhoto = (TextView) findViewById(R.id.id_tack_picture_system_photo);
		mSystemVideo = (TextView) findViewById(R.id.id_tack_picture_system_video);
		mAPIPhoto = (TextView) findViewById(R.id.id_tack_picture_api_photo);
		mAPIVideo = (TextView) findViewById(R.id.id_tack_picture_api_video);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mSystemPhoto.setOnClickListener(this);
		mSystemVideo.setOnClickListener(this);
		mAPIPhoto.setOnClickListener(this);
		mAPIVideo.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.camera_take_picture);
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
		case R.id.id_tack_picture_system_photo:
		{
			//调用系统Camera App实现拍照
			Uri uri = Uri.fromFile(FileUtil.getFile());
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, 0);
			break;
		}
		case R.id.id_tack_picture_system_video:
		{
			//调用系统Camera App实现拍视频
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); 
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, 1);
			break;
		}
		case R.id.id_tack_picture_api_photo:
		{
			Intent intent = new Intent(mContext, CustomCameraActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_tack_picture_api_video:
		{
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0)
		{
			if (resultCode == RESULT_OK)
			{
				//mImageView1.setImageURI(Uri.fromFile(new File(FileUtil.getFilePath())));
			} 
		}
		else if (requestCode == 1)
		{
			if (data != null)
			{
				Uri videoUri = data.getData(); 
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(videoUri, null, null, null, null); 
				cursor.moveToFirst(); //这个必须加，否则下面读取会报错 
				String recordedVideoFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)); 
				int recordedVideoFileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
				Log.e("zhaoqy", " recordedVideoFilePath: " + recordedVideoFilePath);
				Log.e("zhaoqy", " videoSize: " + recordedVideoFileSize); 
				//recordedVideoFilePath: /storage/emulated/0/DCIM/Camera/VID_20150910_102738.mp4
			}
		}
	}
}
