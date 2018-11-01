package com.zhaoqy.app.demo.animation;

import com.zhaoqy.app.demo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnimationScaneActivity extends Activity implements OnClickListener
{
	protected static final int SCAN_LODING = 1;
	protected static final int FINSH_SCAN = 2;
	private Context      mContext;
	private ImageView    mBack;
	private TextView     mTitle;
	private ImageView    im_scan;
	private ImageView    im_dian;
	private TextView     tv_lodingApk;
	private TextView     tv_count;
	private LinearLayout mScanText;
	private ProgressBar  pb_loding;
	private int          count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_scane);
		mContext = this;
		
		initView();
		setListener();
		initData();
		fillData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		im_scan = (ImageView) findViewById(R.id.im_scan);
		im_dian = (ImageView) findViewById(R.id.im_dian);
		tv_lodingApk = (TextView) findViewById(R.id.tv_lodingApk);
		mScanText = (LinearLayout) findViewById(R.id.ll_scanText);
		pb_loding = (ProgressBar) findViewById(R.id.pb_loding);
		tv_count = (TextView) findViewById(R.id.tv_count);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_scane));
		
		RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(2000);
		animation.setRepeatCount(Animation.INFINITE);
		im_scan.startAnimation(animation);
		
		AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
		animation2.setDuration(3000);
		animation2.setRepeatCount(Animation.INFINITE);
		im_dian.startAnimation(animation2);
		
		count = 0;
	}
	
	private void fillData() 
	{
		tv_lodingApk.setText("开始准备释放空闲CPU线程");
		new Thread()
		{
			public void run() 
			{
				pb_loding.setMax(177);
				for (int i = 1; i <= 177; i++) 
				{
					Message msg = Message.obtain();
					msg.what = SCAN_LODING;
					handler.sendMessage(msg);
					count =i;
					pb_loding.setProgress(count);
					try 
					{
						Thread.sleep(100);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				Message msg = Message.obtain();
				msg.what = FINSH_SCAN;
				handler.sendMessage(msg);
			};
		}.start();
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
		default:
			break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case SCAN_LODING:
			{
				tv_lodingApk.setText("正在扫描:第" + count + "个动画");
				TextView tv = new TextView(mContext);
				tv.setTextSize(14);
				tv_count.setText("已扫描:" + count + "个动画");
				tv_count.setTextColor(getResources().getColor(R.color.black));
				tv_count.setGravity(Gravity.CENTER_HORIZONTAL);
				mScanText.addView(tv,0);
				break;
			}
			case FINSH_SCAN:
			{
				tv_lodingApk.setText("扫描完毕");
				im_scan.clearAnimation(); //清除此ImageView身上的动画
				im_dian.clearAnimation(); //清除此ImageView身上的动画
				break;
			}
			default:
				break;
			}
		};
	};
}
