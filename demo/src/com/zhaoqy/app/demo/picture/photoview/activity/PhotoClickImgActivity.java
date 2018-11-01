package com.zhaoqy.app.demo.picture.photoview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.photoview.util.InfoUtil;
import com.zhaoqy.app.demo.picture.photoview.view.PhotoView;

public class PhotoClickImgActivity extends Activity implements OnClickListener, OnCheckedChangeListener
{
	private ImageView  mBack;
	private TextView   mTitle;
	private RadioGroup mRadioGroup;
	private PhotoView  mImg1;
	private PhotoView  mImg2;
	private InfoUtil   mRectF;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_photo_clickimg);

		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mImg1 = (PhotoView) findViewById(R.id.img1);
        mImg2 = (PhotoView) findViewById(R.id.img2);
		mRadioGroup = (RadioGroup) findViewById(R.id.group);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mImg1.setOnClickListener(this);
		mImg2.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("单个Imageview点击放大");
		//设置不可以双指缩放移动放大等操作，跟普通的image一模一样,默认情况下就是disenable()状态
        mImg1.disenable();
		//需要启动缩放需要手动开启
        mImg2.enable();
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
		case R.id.img1:
		{
			 mImg1.setVisibility(View.GONE);
             mImg2.setVisibility(View.VISIBLE);
             //获取img1的信息
             mRectF = mImg1.getInfo();
             //让img2从img1的位置变换到他本身的位置
             mImg2.animaFrom(mRectF);
			break;
		}
		case R.id.img2:
		{
			//让img2从自身位置变换到原来img1图片的位置大小
            mImg2.animaTo(mRectF, new Runnable() 
            {
                @Override
                public void run() 
                {
                    mImg2.setVisibility(View.GONE);
                    mImg1.setVisibility(View.VISIBLE);
                }
            });
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		switch (checkedId) 
		{
		case R.id.center:
			mImg1.setScaleType(ImageView.ScaleType.CENTER);
			break;
		case R.id.center_crop:
			mImg1.setScaleType(ImageView.ScaleType.CENTER_CROP);
			break;
		case R.id.center_inside:
			mImg1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			break;
		case R.id.fit_center:
			mImg1.setScaleType(ImageView.ScaleType.FIT_CENTER);
			break;
		// 建议用了fit_Xy,fit_end,fit_start就不要使用缩放或者animaFrom或animaTo
		case R.id.fit_end:
			mImg1.setScaleType(ImageView.ScaleType.FIT_END);
			break;
		case R.id.fit_start:
			mImg1.setScaleType(ImageView.ScaleType.FIT_START);
			break;
		case R.id.fit_xy:
			mImg1.setScaleType(ImageView.ScaleType.FIT_XY);
			break;
		}
	}
	
	@Override
    public void onBackPressed() 
	{
        if (mImg2.getVisibility() == View.VISIBLE) 
        {
            mImg2.animaTo(mRectF, new Runnable() 
            {
                @Override
                public void run() 
                {
                    mImg2.setVisibility(View.GONE);
                    mImg1.setVisibility(View.VISIBLE);
                }
            });
        } 
        else 
        {
            super.onBackPressed();
        }
    }
}
