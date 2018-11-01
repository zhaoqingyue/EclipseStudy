package com.zhaoqy.app.demo.camera.takepicture.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.takepicture.util.FileUtil;

@SuppressLint("HandlerLeak")
public class EndCameraActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mCancel;
	private TextView  mSure;
	private ImageView mImageView;
	private String    mPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_carema);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mImageView = (ImageView) findViewById(R.id.id_end_camera_photo);
		mCancel = (TextView) findViewById(R.id.id_end_camera_cancle);
		mSure = (TextView) findViewById(R.id.id_end_camera_sure);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		mSure.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.camera_save);
		mPath = getIntent().getExtras().getString("path");
		Bitmap bitmap = FileUtil.getdecodeBitmap(mPath);
		setImageView(bitmap);
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
		case R.id.id_end_camera_cancle:
		{
			finish();
			break;
		}
		case R.id.id_end_camera_sure:
		{
			Bitmap bitmap = getScreenShot(mImageView);
			int result = FileUtil.saveBitmap(mPath, bitmap);
			if (result == 1) 
			{
				Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
			}
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	private void setImageView(Bitmap bitmap) 
	{
		Matrix matrix = new Matrix();
		matrix.preRotate(90);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		mImageView.setImageBitmap(bitmap);
	}
	
	//获取指定Activity的截屏，保存到png文件
	private Bitmap getScreenShot(View photo) 
	{
		//View是你需要截图的View
		View view = photo;
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		
		//获取长和高
		int width = view.getWidth();
		int height = view.getHeight();

		Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);
		view.destroyDrawingCache();
		return b;
	}
}
