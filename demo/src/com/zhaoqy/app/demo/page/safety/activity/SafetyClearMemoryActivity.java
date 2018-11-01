/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyClearMemoryActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 内存优化
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.SafetyClearMemoryAdapter;
import com.zhaoqy.app.demo.page.safety.item.ProcessInfo;
import com.zhaoqy.app.demo.page.safety.util.TrafficDataUtil;

public class SafetyClearMemoryActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private List<RunningAppProcessInfo> mRunningTasks;
	private SafetyClearMemoryAdapter    mAdapter;
	private ActivityManager   mActivityManager;
	private PackageManager    mPackageManager;
	private List<ProcessInfo> mProcessInfoList;
	private ProcessInfo       mProcessInfo;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mRight;
	private TextView  mNum;
	private TextView  mSize;
	private ListView  mListView;
	private ImageView mLoadIcon;
	private View      mLoad;
	private Button    mClear;
	private CheckBox  mCheckBox;
	private Animation mAnimation;
    private int       mCount = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_clear_memory);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRight = (TextView) findViewById(R.id.id_title_right_text);
		mNum = (TextView) findViewById(R.id.clear_memory_num);
		mSize = (TextView) findViewById(R.id.clear_memory_availsize);
		mLoadIcon = (ImageView) findViewById(R.id.clear_memory_loding);
		mLoad =  findViewById(R.id.clear_memory_rl_loading);
		mListView = (ListView) findViewById(R.id.clear_memory_listview);
		mClear = (Button) findViewById(R.id.clear_memory_btn_clear);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("内存优化");
		mBack.setVisibility(View.VISIBLE);
		mRight.setText("定时");
		mRight.setTextColor(0xFF45C01A);
		mRight.setVisibility(View.VISIBLE);
		
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String avaiMemory = getSystemAvailableMemorySize();
		mSize.setText(avaiMemory);
		mProcessInfoList = new ArrayList<ProcessInfo>();
		new Thread(new getProcessInfo()).start();
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
			Intent intent = new Intent(mContext, SafetyClearTimeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_memory_btn_clear:
		{
			if (mCount > 0) 
			{
				clearMore();
			} 
			else 
			{
				Toast.makeText(mContext, "请选择需要清理的软件", Toast.LENGTH_SHORT).show();
			}		
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		ProcessInfo info = mProcessInfoList.get(position);
		mCheckBox = (CheckBox) view.findViewById(R.id.clear_memory_cb);
		if (info.isChecked()) 
		{
			mCheckBox.setChecked(true);
			info.setChecked(false);
			mCount++;
		} 
		else 
		{
			if(mCount > 0)
			{
				mCount--;
			}
			mCheckBox.setChecked(false);
			info.setChecked(true);			
		}
		
		if(mCount > 0)
		{
			mClear.setText("一键清理(" + mCount + ")");			
		}
		else
		{
			mClear.setText("一键清理");
		}
	}

	private void clearMore() 
	{
		int total = 0;
		int memorySize = 0;
		for (ProcessInfo infos : mProcessInfoList) 
		{
			if (infos.isChecked()) 
			{
				ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				am.killBackgroundProcesses(infos.getPkgname());
				total++;
				memorySize += infos.getMemSize();
				mProcessInfoList.remove(infos);
				break;
			}
		}
		Toast.makeText(mContext, "清理了" + total + "个进程," + "释放了" + TrafficDataUtil.getMemoryData(memorySize) + "内存", 
				Toast.LENGTH_SHORT).show();
		mAdapter = new SafetyClearMemoryAdapter(mContext, mProcessInfoList);
		mListView.setAdapter(mAdapter);
		mClear.setText("一键清理");
	}

	private void showAnim() 
	{
		mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.safety_rotate);
		mLoadIcon.startAnimation(mAnimation);
	}

	private void stopAnim() 
	{
		mLoadIcon.clearAnimation();
		mLoad.setVisibility(View.INVISIBLE);
	}
	
	class getProcessInfo implements Runnable 
	{
		@Override
		public void run() 
		{
			mHandler.sendEmptyMessage(1);
			getRunningAppProcessInfo(mContext);
			mHandler.sendEmptyMessage(2);
			mHandler.sendEmptyMessage(3);
			mClear.setOnClickListener(SafetyClearMemoryActivity.this);
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
			case 1:
			{
				showAnim();
				break;
			}
			case 2:
			{
				stopAnim();
				break;
			}
			case 3:
			{
				mAdapter = new SafetyClearMemoryAdapter(mContext, mProcessInfoList);
				mListView.setAdapter(mAdapter);
				mNum.setText(String.valueOf(mProcessInfoList.size()));
				break;
			}
			default:
				break;
			}
		};
	};
	
	private String getSystemAvailableMemorySize() 
	{
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		mActivityManager.getMemoryInfo(outInfo);
		String availMem = Formatter.formatFileSize(mContext, outInfo.availMem);
		return availMem;
	}

	public void getRunningAppProcessInfo(Context context) 
	{
		try 
		{
			//获得包管理器
			mPackageManager = getPackageManager();
			//获得所有正在进行的程序存放在一个list中
			mRunningTasks = mActivityManager.getRunningAppProcesses(); 
			for (RunningAppProcessInfo infos : mRunningTasks) 
			{
				//获得PackageInfo对象
				PackageInfo pInfo = new PackageInfo(context);
				String processName = infos.processName;
				if ("system".equals(processName)
						|| "com.mobilesafe".equals(processName)
						|| "com.android.phone".equals(processName)
						|| "android.process.acore".equals(processName)
						|| "android.process.media".equals(processName)) 
				{
					continue;
				}
				mProcessInfo = new ProcessInfo(mContext);
				mProcessInfo.setAppIcon(pInfo.getInfo(processName).loadIcon(mPackageManager));
				mProcessInfo.setAppLabel(pInfo.getInfo(processName).loadLabel(mPackageManager).toString());
				mProcessInfo.setPkgname(processName.toString());
				mProcessInfo.Checked = true;
				final android.os.Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(new int[] { infos.pid });
				long memory = memoryInfo[0].getTotalPrivateDirty();
				mProcessInfo.setMemSize(memory);
				mProcessInfoList.add(mProcessInfo);
			}
		} 
		catch (Exception ex) 
		{
		}
	}

	class PackageInfo 
	{
		private List<ApplicationInfo> mAppList;

		public PackageInfo(Context context) 
		{
			PackageManager pm = context.getApplicationContext().getPackageManager();
			mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		}

		public ApplicationInfo getInfo(String name) 
		{
			if (name == null) 
			{
				return null;
			}
			for (ApplicationInfo appInfo : mAppList) 
			{
				if (name.equals(appInfo.processName)) 
				{
					return appInfo;
				}
			}
			return null;
		}
	}
}
