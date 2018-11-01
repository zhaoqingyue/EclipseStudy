package com.zhaoqy.app.demo.camera.takepicture.activity;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.activity.GalleryActivity;
import com.zhaoqy.app.demo.camera.takepicture.util.FileUtil;

public class CustomCameraActivity extends Activity implements OnClickListener
{
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private SurfaceView mSurfaceView;
	private ImageView   mGallery;
	private ImageView   mTackPic;
	private TextView    mMore;
	private Camera      camera;
	private Camera.Parameters parameters = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_camera);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSurfaceView = (SurfaceView) findViewById(R.id.id_custom_surfaceView);
		mGallery = (ImageView) findViewById(R.id.id_custom_camera_gallery);
		mTackPic = (ImageView) findViewById(R.id.id_custom_tack_picture);
		mMore = (TextView) findViewById(R.id.id_custom_camera_more);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mGallery.setOnClickListener(this);
		mTackPic.setOnClickListener(this);
		mMore.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.camera);
		
		mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.getHolder().setFixedSize(176, 144); //surfaceView 分辨率
		mSurfaceView.getHolder().setKeepScreenOn(true);
		mSurfaceView.getHolder().addCallback(new SurfaceViewCallback());
	}

	private class TakePictureCallback implements PictureCallback 
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera) 
		{
			String path = FileUtil.getFilePath();
			int result = FileUtil.saveBitmap(path, data);
			Log.e("zhaoqy", " result: " + result);
			if (result == 1)
			{
				Intent intent=new Intent(mContext, EndCameraActivity.class);
				intent.putExtra("path", path);
				startActivity(intent);
			}
			else
			{
				camera.startPreview();
			}
		}
	}

	private class SurfaceViewCallback implements Callback 
	{
		@SuppressWarnings("deprecation")
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
		{
			parameters = camera.getParameters();           //设置相机参数
			parameters.setPictureFormat(PixelFormat.JPEG); //图片格式
			parameters.setPreviewSize(width, height);      //预览大小
			parameters.setPreviewFrameRate(5);             //帧
			parameters.setPictureSize(width, height);      //保存图片大小
			parameters.setJpegQuality(80);                 //图片质量
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) 
		{
			try 
			{
				camera = Camera.open();
				camera.setPreviewDisplay(holder);
				camera.setDisplayOrientation(getPreviewDegree(CustomCameraActivity.this));
				camera.startPreview();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) 
		{
			if (camera != null) 
			{
				camera.release();
				camera = null;
			}
		}
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if (camera != null) 
		{
			camera.release();
			camera = null;
		}
	}

	public static int getPreviewDegree(Activity activity) 
	{
		//获得手机的方向
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degree = 0;
		//根据手机的方向计算相机预览画面应该选择的角度
		switch (rotation) 
		{
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 0;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;
		}
		return degree;
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
		case R.id.id_custom_camera_gallery:
		{
			Intent intent = new Intent(mContext, GalleryActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_custom_tack_picture:
		{
			if (camera != null) 
			{
				camera.takePicture(null, null, new TakePictureCallback());
			}
			break;
		}
		case R.id.id_custom_camera_more:
		{
			break;
		}
		default:
			break;
		}
	}
}
