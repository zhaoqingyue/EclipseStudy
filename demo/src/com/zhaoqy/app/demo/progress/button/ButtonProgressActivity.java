package com.zhaoqy.app.demo.progress.button;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;

public class ButtonProgressActivity extends Activity implements OnClickListener
{
	private MasterLayout mMasterLayout;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_button);
        mContext = this;
        
        initView();
        setListener();
        initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mMasterLayout = (MasterLayout) findViewById(R.id.MasterLayout01);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mMasterLayout.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.progress_item3);
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
		case R.id.MasterLayout01:
		{
			//Need to call this method for animation and progression
			mMasterLayout.animation(); 
			
			if (mMasterLayout.flg_frmwrk_mode == 1) 
			{ 
				//Start state. Call any method that you want to execute
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						Toast.makeText(mContext, "Starting download", Toast.LENGTH_SHORT).show();
					}
				});
				new DownLoadSigTask().execute();
			}
			if (mMasterLayout.flg_frmwrk_mode == 2) 
			{
				//Running state. Call any method that you want to execute
				new DownLoadSigTask().cancel(true);
				mMasterLayout.reset();
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						Toast.makeText(mContext, "Download stopped", Toast.LENGTH_SHORT).show();
					}
				});
			}
			if (mMasterLayout.flg_frmwrk_mode == 3) 
			{
				//End state. Call any method that you want to execute.
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						Toast.makeText(mContext, "Download complete", Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		}
		default:
			break;
		}
	}
	
	class DownLoadSigTask extends AsyncTask<String, Integer, String> 
	{
		@Override
		protected void onPreExecute() 
		{
		}

		@Override
		protected String doInBackground(final String... args) 
		{
			//Creating dummy task and updating progress
			for (int i = 0; i <= 100; i++) 
			{
				try 
				{
					Thread.sleep(50);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				publishProgress(i);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) 
		{
			//publishing progress to progress arc
			mMasterLayout.cusview.setupprogress(progress[0]);
		}
	}
}
