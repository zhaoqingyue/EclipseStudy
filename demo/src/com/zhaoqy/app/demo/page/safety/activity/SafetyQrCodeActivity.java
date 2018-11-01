/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyQrCodeActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 安全二维码
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.camera.CameraManager;
import com.zhaoqy.app.demo.page.safety.camera.CaptureActivityHandler;
import com.zhaoqy.app.demo.page.safety.camera.InactivityTimer;
import com.zhaoqy.app.demo.page.safety.camera.RGBLuminanceSource;
import com.zhaoqy.app.demo.page.safety.view.ViewfinderView;

public class SafetyQrCodeActivity extends Activity implements Callback, OnClickListener 
{
	public static final int REQUEST_CODE = 1;
	public static final int PARSE_BARCODE_SUC = 2;
	public static final int PARSE_BARCODE_FAIL = 3;
	public static final float BEEP_VOLUME = 0.10f;
	public static final long VIBRATE_DURATION = 200L;

	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mRight;

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private Vector<BarcodeFormat> decodeFormats;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private LinearLayout codemain_photo;
	private Bitmap scanBitmap;
	private ProgressDialog mProgress;
	private String characterSet;
	private String photo_path;
	private boolean hasSurface;
	private boolean playBeep;
	private boolean vibrate;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_qrcode);
		mContext = this;

		initView();
		initData();
		setListener();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() 
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) 
		{
			initCamera(surfaceHolder);
		} 
		else 
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) 
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRight = (TextView) findViewById(R.id.id_title_right_text);

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		codemain_photo = (LinearLayout) findViewById(R.id.codemain_photo);
		txtResult = (TextView) findViewById(R.id.txtResult);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		codemain_photo.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("安全二维码");
		mBack.setVisibility(View.VISIBLE);
		mRight.setText("设置");
		mRight.setTextColor(0xFF45C01A);
		mRight.setVisibility(View.VISIBLE);

		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	private void initCamera(SurfaceHolder surfaceHolder) 
	{
		try 
		{
			CameraManager.get().openDriver(surfaceHolder);
		} 
		catch (IOException ioe) 
		{
			return;
		} 
		catch (RuntimeException e) 
		{
			return;
		}
		if (handler == null) 
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
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
		case R.id.id_title_right_text:
		{
			Intent intent = new Intent(mContext, SafetyQrCodeSettingActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.codemain_photo:
		{
			Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
			innerIntent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
			startActivityForResult(wrapperIntent, REQUEST_CODE);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		if (!hasSurface) 
		{
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() 
	{
		return viewfinderView;
	}

	public Handler getHandler() 
	{
		return handler;
	}

	public void drawViewfinder() 
	{
		viewfinderView.drawViewfinder();
	}

	public void handleDecode(Result obj, Bitmap barcode) 
	{
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		txtResult.setText(obj.getBarcodeFormat().toString() + ":" + obj.getText());
		
		if (obj.getBarcodeFormat().toString().equals("QR_CODE")) 
		{
			Uri uri = Uri.parse(obj.getText());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			txtResult.setText("");
		}
	}

	private void initBeepSound() 
	{
		if (playBeep && mediaPlayer == null) 
		{
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd( R.raw.beep);
			try 
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} 
			catch (IOException e) 
			{
				mediaPlayer = null;
			}
		}
	}

	private void playBeepSoundAndVibrate() 
	{
		if (playBeep && mediaPlayer != null) 
		{
			mediaPlayer.start();
		}
		if (vibrate) 
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() 
	{
		public void onCompletion(MediaPlayer mediaPlayer) 
		{
			mediaPlayer.seekTo(0);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK) 
		{
			switch (requestCode) 
			{
			case REQUEST_CODE:
				// 获取选中图片的路径
				Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
				if (cursor.moveToFirst()) 
				{
					photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				}
				cursor.close();
				mProgress = new ProgressDialog(mContext);
				mProgress.setMessage("正在扫描...");
				mProgress.setCancelable(false);
				mProgress.show();

				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						Result result = scanningImage(photo_path);
						if (result != null) 
						{
							Message m = mHandler.obtainMessage();
							m.what = PARSE_BARCODE_SUC;
							m.obj = result.getText();
							mHandler.sendMessage(m);
							mProgress.dismiss();
						} 
						else 
						{
							Message m = mHandler.obtainMessage();
							m.what = PARSE_BARCODE_FAIL;
							m.obj = "Scan failed!";
							mHandler.sendMessage(m);
							mProgress.dismiss();
						}
					}
				}).start();
				break;
			}
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case PARSE_BARCODE_SUC:
				String str1 = msg.obj.toString();
				txtResult.setText(str1);
				Uri uri = Uri.parse(str1);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				break;
			case PARSE_BARCODE_FAIL:
				String str2 = msg.obj.toString();
				txtResult.setText(str2);
				break;
			}
		}
	};

	// 解析手机相册图片的二维码
	public Result scanningImage(String path) 
	{
		if (TextUtils.isEmpty(path)) 
		{
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try 
		{
			return reader.decode(bitmap1, hints);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		if (handler != null) 
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() 
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}
}
