package com.zhaoqy.app.demo.page.way.activity;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import com.zhaoqy.app.demo.R;

public class WayShareActivity extends Activity implements OnClickListener
{
	private ImageView mShare0;
	private ImageView mShare1;
	private ImageView mShare2;
	private ImageView mShare3;
	private ImageView mShare4;
	private ImageView mShare5;
	private ImageView mShare6;
	private ImageView mShare7;
	private ImageView mShare8;
	private String    mShareContent;
	private Platform  mTecent;
	private Platform  mSina;
	private Platform  mRenren;
	private Platform  mQzone;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_way_share);
		
		initViews();
		setListener();
		initData();
	}
	
	private void initViews() 
	{
		mShare0 = (ImageView) findViewById(R.id.id_way_share_item0);
		mShare1 = (ImageView) findViewById(R.id.id_way_share_item1);
		mShare2 = (ImageView) findViewById(R.id.id_way_share_item2);
		mShare3 = (ImageView) findViewById(R.id.id_way_share_item3);
		mShare4 = (ImageView) findViewById(R.id.id_way_share_item4);
		mShare5 = (ImageView) findViewById(R.id.id_way_share_item5);
		mShare6 = (ImageView) findViewById(R.id.id_way_share_item6);
		mShare7 = (ImageView) findViewById(R.id.id_way_share_item7);
		mShare8 = (ImageView) findViewById(R.id.id_way_share_item8);
	}

	private void setListener() 
	{
		mShare0.setOnClickListener(this);
		mShare1.setOnClickListener(this);
		mShare2.setOnClickListener(this);
		mShare3.setOnClickListener(this);
		mShare4.setOnClickListener(this);
		mShare5.setOnClickListener(this);
		mShare6.setOnClickListener(this);
		mShare7.setOnClickListener(this);
		mShare8.setOnClickListener(this);
	}
	
	private void initData()
	{
		ShareSDK.initSDK(getApplicationContext());
		mShareContent = getIntent().getStringExtra("shareContent");
		mTecent = ShareSDK.getPlatform(TencentWeibo.NAME);
		mSina = ShareSDK.getPlatform(SinaWeibo.NAME);
		mRenren = ShareSDK.getPlatform(Renren.NAME);
		mQzone = ShareSDK.getPlatform(QZone.NAME);
		setPlatformlistener(mTecent, mSina, mRenren, mQzone);
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_way_share_item0:
		{
			mTecent.authorize();
			finish();
			break;
		}
		case R.id.id_way_share_item1:
		{
			mSina.authorize();
			finish();
			break;
		}
		case R.id.id_way_share_item2:
		{
			mRenren.authorize();
			finish();
			break;
		}
		case R.id.id_way_share_item3:
		{
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		}
		case R.id.id_way_share_item4:
		{
			mQzone.authorize();
			finish();
			break;
		}
		
		case R.id.id_way_share_item5:
		{
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		}
		case R.id.id_way_share_item6:
		{
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		}
		case R.id.id_way_share_item7:
		{
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		}
		case R.id.id_way_share_item8:
		{
			Toast.makeText(getApplicationContext(), "本接口暂未实现，敬请期待", 1).show();
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	private void setPlatformlistener(final Platform tecentplatform, final Platform sinaplatform, final Platform renrenPlatform, final Platform qzonePlatform) 
	{
		tecentplatform.setPlatformActionListener(new PlatformActionListener() 
		{
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) 
			{
			}
			
			@SuppressLint("ShowToast")
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) 
			{
				TencentWeibo.ShareParams shareParams = new TencentWeibo.ShareParams();
				shareParams.setText("" + mShareContent);
				tecentplatform.share(shareParams);
				Toast.makeText(getApplicationContext(), "分享成功", 1).show();
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) 
			{
			}
		});
		
		sinaplatform.setPlatformActionListener(new PlatformActionListener() 
		{
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) 
			{
			}
			
			@SuppressLint("ShowToast")
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) 
			{
				SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();
				shareParams.setText("" + mShareContent);
				sinaplatform.share(shareParams);
				Toast.makeText(getApplicationContext(), "分享成功", 1).show();
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1)
			{
			}
		});
		
		renrenPlatform.setPlatformActionListener(new PlatformActionListener() 
		{
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) 
			{
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) 
			{
				Renren.ShareParams shareParams = new Renren.ShareParams();
				shareParams.setText("" + mShareContent);
				renrenPlatform.share(shareParams);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) 
			{
			}
		});
		
		qzonePlatform.setPlatformActionListener(new PlatformActionListener() 
		{
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) 
			{
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) 
			{
				QZone.ShareParams shareParams = new QZone.ShareParams();
				shareParams.setText("" + mShareContent);
				qzonePlatform.share(shareParams);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) 
			{
			}
		});
	}
}
