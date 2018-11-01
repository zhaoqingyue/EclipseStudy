package com.zhaoqy.app.demo.animation;

import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class AnimationFrameLayoutActivity extends Activity implements OnClickListener
{
	public static final String[] keywords = { "QQ", "BaseAnimation", "APK", "GFW", "铅笔", "短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛", "Base", 
		"笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人", "内存清理", "地图", "导航", "闹钟", "主题", "通讯录", "播放器", "CSDN leak", "安全", "Animation", "美女",
		"天气", "4743G", "戴尔", "联想", "欧朋",  "浏览器", "愤怒的小鸟", "mmShow", "网易公开课", "iciba", "油水关系", "网游App", "互联网", "365日历", "脸部识别", 
	    "Chrome", "Safari", "中国版Siri", "苹果", "iPhone5S", "摩托 ME525", "魅族 MX3", "小米" };
	private ImageView    mBack;
	private TextView     mTitle;
	private KeywordsFlow mKeywordsFlow;
	private Button       mIn;
	private Button       mOut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_custom_framelayout);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mKeywordsFlow = (KeywordsFlow) findViewById(R.id.frameLayout1);
		mIn = (Button) findViewById(R.id.id_animation_framelayout_in);
		mOut = (Button) findViewById(R.id.id_animation_framelayout_out);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mIn.setOnClickListener(this);
		mOut.setOnClickListener(this);
		mKeywordsFlow.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_custom_item0));
		mKeywordsFlow.setDuration(800l);
		feedKeywordsFlow(mKeywordsFlow, keywords);
		mKeywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
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
		case R.id.id_animation_framelayout_in:
		{
			mKeywordsFlow.rubKeywords();
			feedKeywordsFlow(mKeywordsFlow, keywords);
			mKeywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
			break;
		}
		case R.id.id_animation_framelayout_out:
		{
			mKeywordsFlow.rubKeywords();
			feedKeywordsFlow(mKeywordsFlow, keywords);
			mKeywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
			break;
		}
		default:
			break;
		}
	}
	
	private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) 
	{
		Random random = new Random();
		for (int i = 0; i < KeywordsFlow.MAX; i++) 
		{
			int ran = random.nextInt(arr.length);
			String tmp = arr[ran];
			keywordsFlow.feedKeyword(tmp);
		}
	}
}
