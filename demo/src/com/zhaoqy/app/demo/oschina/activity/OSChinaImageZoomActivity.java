/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaImageZoomActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description:  图片缩放对话框
 * @author: zhaoqy
 * @date: 2015-11-23 上午10:46:41
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.common.ApiClient;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.util.FileUtils;
import com.zhaoqy.app.demo.oschina.util.ImageUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

@SuppressLint({ "HandlerLeak", "FloatMath" })
public class OSChinaImageZoomActivity extends OSChinaBaseActivity implements OnTouchListener, OnClickListener
{
	private static final double ZOOM_IN_SCALE = 1.25; //放大系数
	private static final double ZOOM_OUT_SCALE = 0.8; //缩小系数
	private static final float  MAX_SCALE = 4f;       //最大缩放比例
	private static final int NONE = 0; //初始状态
	private static final int DRAG = 1; //拖动
	private static final int ZOOM = 2; //缩放
	
	private DisplayMetrics mDisplayMetrics;
	private ViewSwitcher   mViewSwitcher;
	private Matrix    mMatrix = new Matrix();
	private Matrix    mSavedMatrix = new Matrix();
	private PointF    mStart = new PointF();
	private PointF    mMid = new PointF();
	private Context   mContext;
	private ImageView mImgView;
	private Button    mZoomIn;
	private Button    mZoomOut;
	private Bitmap    mBitmap;
	private Bitmap    mZoomedMap;
	private float     mScaleWidth = 1;
	private float     mScaleHeight = 1;
	private float     mMinScaleR;// 最小缩放比例
	private float     mOldDist = 1f;
	private int       mMode = NONE;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_image_zoom);
		mContext = this;
		
		initView();  
		setListener();
        initData();
	}
	
	private void initView()
    {
		mImgView = (ImageView) findViewById(R.id.imagezoomdialog_image);
		mViewSwitcher = (ViewSwitcher)findViewById(R.id.imagezoomdialog_view_switcher); 
    } 
	
	private void setListener() 
	{
		mImgView.setOnTouchListener(this);
	}
	
    private void initData() 
    {
    	mDisplayMetrics = new DisplayMetrics();
		//获取分辨率
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics); 
    	
		final String imgURL = getIntent().getStringExtra("img_url");		
		final String ErrMsg = "图片加载失败";
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what==1 && msg.obj != null)
				{
					mBitmap = (Bitmap)msg.obj;
					mImgView.setImageBitmap(mBitmap);
					minZoom();   //计算最小缩放比
					CheckView(); //设置图像居中
					mImgView.setImageMatrix(mMatrix);
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
						bmp = BitmapFactory.decodeResource(mImgView.getResources(), R.drawable.oschina_dface);
					}
					if(bmp == null)
					{
						//是否有缓存图片
				    	//Environment.getExternalStorageDirectory();返回/sdcard
				    	String filepath = getFilesDir() + File.separator + filename;
						File file = new File(filepath);
						if(file.exists())
						{
							bmp = ImageUtils.getBitmap(mImgView.getContext(), filename);
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
								ImageUtils.saveImage(mImgView.getContext(), filename, bmp);
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
							}
							//缩放图片
							bmp = ImageUtils.reDrawBitMap(OSChinaImageZoomActivity.this, bmp);
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
	
	public boolean onTouch(View v, MotionEvent event) 
	{
		ImageView mImgView = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) 
		{
		case MotionEvent.ACTION_DOWN:
		{
			//设置拖拉模式(主点)
			mSavedMatrix.set(mMatrix);
			mStart.set(event.getX(), event.getY());
			mMode = DRAG;
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN:
		{
			//设置多点触摸模式(副点)
			mOldDist = spacing(event);
			if (mOldDist > 10f) 
			{
				mSavedMatrix.set(mMatrix);
				midPoint(mMid, event);
				mMode = ZOOM;
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		{
			mMode = NONE;
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			//若为DRAG模式，则点击移动图片
			if (mMode == DRAG) 
			{
				mMatrix.set(mSavedMatrix);
				//设置位移
				mMatrix.postTranslate(event.getX() - mStart.x, event.getY() - mStart.y);
			}
			else if (mMode == ZOOM) 
			{
				//若为ZOOM模式，则多点触摸缩放
				float newDist = spacing(event);
				if (newDist > 10f) 
				{
					mMatrix.set(mSavedMatrix);
					float scale = newDist / mOldDist;
					//设置缩放比例和图片中点位置
					mMatrix.postScale(scale, scale, mMid.x, mMid.y);
				}
			}
			break;
		}
		default:
			break;
		}

		mImgView.setImageMatrix(mMatrix);
		CheckView();
		return true; 
	}

    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void CheckView() 
    {
        float p[] = new float[9];
        mMatrix.getValues(p);
        if (mMode == ZOOM) 
        {
            if (p[0] < mMinScaleR) {
            	mMatrix.setScale(mMinScaleR, mMinScaleR);
            }
            if (p[0] > MAX_SCALE) 
            {
            	mMatrix.set(mSavedMatrix);
            }
        }
        center();
    }

    /**
     * 最小缩放比例，最大为100%
     */
    private void minZoom() 
    {
        if(mBitmap.getWidth() >= mDisplayMetrics.widthPixels)
        {
        	mMinScaleR = ((float) mDisplayMetrics.widthPixels) / mBitmap.getWidth();
        }
    	else
    	{
    		mMinScaleR = 1.0f;
    	}
        
        if (mMinScaleR < 1.0) 
        {
        	mMatrix.postScale(mMinScaleR, mMinScaleR);
        }
    }

    private void center() 
    {
        center(true, true);
    }

    /**
     * 横向、纵向居中
     */
    protected void center(boolean horizontal, boolean vertical) 
    {
        Matrix m = new Matrix();
        m.set(mMatrix);
        RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;

        if (vertical) 
        {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = mDisplayMetrics.heightPixels;
            if (height < screenHeight) 
            {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } 
            else if (rect.top > 0) 
            {
                deltaY = -rect.top;
            }
            else if (rect.bottom < screenHeight) 
            {
                deltaY = mImgView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) 
        {
            int screenWidth = mDisplayMetrics.widthPixels;
            if (width < screenWidth) 
            {
                deltaX = (screenWidth - width) / 2 - rect.left;
            }
            else if (rect.left > 0) 
            {
                deltaX = -rect.left;
            } 
            else if (rect.right < screenWidth) 
            {
                deltaX = screenWidth - rect.right;
            }
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }
	
	// 计算移动距离
	private float spacing(MotionEvent event) 
	{
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 计算中点位置
	private void midPoint(PointF point, MotionEvent event) 
	{
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// 放大，缩小按钮点击事件
	//@Override
	public void onClick(View v) 
	{
		if (v == mZoomIn) 
		{
			enlarge();
		} 
		else if (v == mZoomOut) 
		{
			small();
		}
	}

	// 按钮点击缩小函数
	private void small() 
	{
		int bmpWidth = mBitmap.getWidth();
		int bmpHeight = mBitmap.getHeight();

		mScaleWidth = (float) (mScaleWidth * ZOOM_OUT_SCALE);
		mScaleHeight = (float) (mScaleHeight * ZOOM_OUT_SCALE);

		Matrix matrix = new Matrix();
		matrix.postScale(mScaleWidth, mScaleHeight);
		mZoomedMap = Bitmap.createBitmap(mBitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
		mImgView.setImageBitmap(mZoomedMap);
	}

	// 按钮点击放大函数
	private void enlarge() 
	{
		try 
		{
			int bmpWidth = mBitmap.getWidth();
			int bmpHeight = mBitmap.getHeight();

			mScaleWidth = (float) (mScaleWidth * ZOOM_IN_SCALE);
			mScaleHeight = (float) (mScaleHeight * ZOOM_IN_SCALE);

			Matrix matrix = new Matrix();
			matrix.postScale(mScaleWidth, mScaleHeight);
			mZoomedMap = Bitmap.createBitmap(mBitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
			mImgView.setImageBitmap(mZoomedMap);
		} 
		catch (Exception e) 
		{
			//can't zoom because of memory issue, just ignore, no big deal
		}
	}
}
