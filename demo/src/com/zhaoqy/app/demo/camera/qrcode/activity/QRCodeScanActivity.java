package com.zhaoqy.app.demo.camera.qrcode.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.qrcode.camera.CameraManager;
import com.zhaoqy.app.demo.camera.qrcode.camera.CaptureActivityHandler;
import com.zhaoqy.app.demo.camera.qrcode.camera.InactivityTimer;
import com.zhaoqy.app.demo.camera.qrcode.view.QRCodeScanView;
import com.zhaoqy.app.demo.weixin.ui.activity.MyCodeActivity;

public class QRCodeScanActivity extends Activity implements Callback, OnClickListener
{
	private static final long VIBRATE_DURATION = 200L;
	private static final float BEEP_VOLUME = 0.10f;
	private Context                mContext;
	private CaptureActivityHandler mHandler;
	private Vector<BarcodeFormat>  mDecodeFormat;
	private InactivityTimer mInactivityTimer;
	private MediaPlayer     mMediaPlayer;
	private SurfaceView     mSurfaceView;
	private QRCodeScanView  mQRCodeScanView;
	private ImageView       mBack;
	private TextView        mTitle;
	private Button          mPicture;
	private Button          mFlash;
	private Button          mQrcode;
	private View            mDialogView;
	private Button          mCancle;
	private Button          mSure;
	private TextView        mUrl;
	private Dialog          mDialog;
	private String          mCharacterSet;
	private String          mResultString = "";
	private boolean         mPlayBeep;
	private boolean         mHasSurface;
	private boolean         mVibrate;
	private int             mScreenWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode_scan);
		mContext = this;
		CameraManager.init(getApplication());
		initView();
	    setListener();
	    initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mQRCodeScanView = (QRCodeScanView) findViewById(R.id.id_qrcode_scan_view);
		mSurfaceView = (SurfaceView) findViewById(R.id.id_qrcode_scan_surface);
		mPicture = (Button) findViewById(R.id.id_qrcode_scan_pictrue);
		mFlash = (Button) findViewById(R.id.id_qrcode_scan_flash);
		mQrcode = (Button) findViewById(R.id.id_qrcode_scan_qrcode);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
		mPicture.setOnClickListener(this);
		mFlash.setOnClickListener(this);
		mQrcode.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mTitle.setText(R.string.weixin_find_scan);
		mBack.setVisibility(View.VISIBLE);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mHasSurface = false;
		mInactivityTimer = new InactivityTimer(this);
	}
	
	private void initCustomerDialog()
	{
		mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_scan, null);
		mCancle = (Button) mDialogView.findViewById(R.id.id_dialog_scan_cancle);
		mSure = (Button) mDialogView.findViewById(R.id.id_dialog_scan_sure);
		mUrl = (TextView) mDialogView.findViewById(R.id.id_dialog_scan_url);
		
		mCancle.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				mDialog.dismiss();
				stop();
				start();
			}
		});
		
		mSure.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent= new Intent();        
                intent.setAction("android.intent.action.VIEW");  
                intent.setData(Uri.parse(mResultString));           
                intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");   
                startActivity(intent);
                finish();
			}
		});
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		start();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		if (mHandler != null) 
		{
			mHandler.quitSynchronously();
			mHandler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	public void onDestroy() 
	{
		mInactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(Result result, Bitmap barcode) 
	{
		mInactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		mResultString = result.getText();
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("result", mResultString);
		resultIntent.putExtras(bundle);
		setResult(RESULT_OK, resultIntent);

		//必须没错初始化，否则dialog报错
		initCustomerDialog();
		mUrl.setText(mResultString);
		mDialog = new Dialog(mContext, R.style.scan_dialog);
		mDialog.addContentView(mDialogView, new LayoutParams(mScreenWidth-60, LayoutParams.WRAP_CONTENT));
		mDialog.show();
	}
	
	/**
	 * 开始扫描
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void start()
	{
		SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
		if (mHasSurface) 
		{
			initCamera(surfaceHolder);
		} 
		else 
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		mDecodeFormat = null;
		mCharacterSet = null;
		mPlayBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) 
		{
			mPlayBeep = false;
		}
		initBeepSound();
		mVibrate = true;
	}
	
	/**
	 * 停止扫描
	 */
	private void stop() 
	{
		if (mHandler != null) 
		{
			mHandler.quitSynchronously();
			mHandler = null;
		}
		CameraManager.get().closeDriver();
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
		if (mHandler == null) 
		{
			mHandler = new CaptureActivityHandler(this, mDecodeFormat, mCharacterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		if (!mHasSurface) 
		{
			mHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		mHasSurface = false;
	}

	public QRCodeScanView getViewfinderView() 
	{
		return mQRCodeScanView;
	}

	public Handler getHandler() 
	{
		return mHandler;
	}

	public void drawViewfinder() 
	{
		mQRCodeScanView.drawViewfinder();
	}

	/**
	 * 扫描正确后的震动声音,如果感觉apk大了,可以删除
	 */
	private void initBeepSound() 
	{
		if (mPlayBeep && mMediaPlayer == null) 
		{
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try 
			{
				mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mMediaPlayer.prepare();
			} 
			catch (IOException e) 
			{
				mMediaPlayer = null;
			}
		}
	}

	private void playBeepSoundAndVibrate() 
	{
		if (mPlayBeep && mMediaPlayer != null) 
		{
			mMediaPlayer.start();
		}
		if (mVibrate) 
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() 
	{
		public void onCompletion(MediaPlayer mediaPlayer) 
		{
			mediaPlayer.seekTo(0);
		}
	};

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
		case R.id.id_qrcode_scan_pictrue:
		{
			break;
		}
		case R.id.id_qrcode_scan_flash:
		{
			break;
		}
		case R.id.id_qrcode_scan_qrcode:
		{
			stop();
			Intent intent = new Intent(mContext, MyCodeActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
