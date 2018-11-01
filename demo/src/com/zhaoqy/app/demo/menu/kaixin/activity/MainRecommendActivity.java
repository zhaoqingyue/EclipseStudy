/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainRecommendActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 游戏
 * @author: zhaoqy
 * @date: 2015-11-12 上午9:15:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.RecommendResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MainRecommendActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private RecommendAdapter mAdapter;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ListView  mDisplay;
	private Button    mOfficial;
	private Button    mAppDownLoad;
	private boolean   mIsOfficial = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_recommend);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (ListView) findViewById(R.id.recommend_display);
		mOfficial = (Button) findViewById(R.id.recommend_official);
		mAppDownLoad = (Button) findViewById(R.id.recommend_appdownload);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mOfficial.setOnClickListener(this);
		mAppDownLoad.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("推荐应用");
		getOfficial();
		getAppDownLoad();
		mAdapter = new RecommendAdapter(mApplication.mMyRecommendOfficialResults);
		mDisplay.setAdapter(mAdapter);
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
		case R.id.recommend_official:
		{
			if (!mIsOfficial) 
			{
				mIsOfficial = true;
				mOfficial.setBackgroundResource(R.drawable.menu_kaixin_bottom_left_selected);
				mAppDownLoad.setBackgroundResource(R.drawable.menu_kaixin_bottom_right);
				mAdapter = new RecommendAdapter(mApplication.mMyRecommendOfficialResults);
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		case R.id.recommend_appdownload:
		{
			if (mIsOfficial) 
			{
				mIsOfficial = false;
				mOfficial.setBackgroundResource(R.drawable.menu_kaixin_bottom_left);
				mAppDownLoad.setBackgroundResource(R.drawable.menu_kaixin_bottom_right_selected);
				mAdapter = new RecommendAdapter(mApplication.mMyRecommendAppDownLoadResults);
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取官方模块数据
	 */
	private void getOfficial() 
	{
		if (mApplication.mMyRecommendOfficialResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/recommend_official.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				RecommendResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new RecommendResult();
					result.setName(array.getJSONObject(i).getString("name"));
					result.setIcon(array.getJSONObject(i).getString("icon"));
					result.setTitle(array.getJSONObject(i).getJSONObject("title").getBoolean("istitle"));
					result.setTitleName(array.getJSONObject(i).getJSONObject("title").getString("titlename"));
					mApplication.mMyRecommendOfficialResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取应用下载数据
	 */
	private void getAppDownLoad() 
	{
		if (mApplication.mMyRecommendAppDownLoadResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/recommend_appdownload.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				RecommendResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new RecommendResult();
					result.setName(array.getJSONObject(i).getString("name"));
					result.setIcon(array.getJSONObject(i).getString("icon"));
					result.setDescription(array.getJSONObject(i).getString("description"));
					mApplication.mMyRecommendAppDownLoadResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private class RecommendAdapter extends BaseAdapter 
	{
		private List<RecommendResult> mResults;

		public RecommendAdapter(List<RecommendResult> results) 
		{
			if (results != null) 
			{
				mResults = results;
			} 
			else 
			{
				mResults = new ArrayList<RecommendResult>();
			}
		}

		public int getCount() 
		{
			return mResults.size();
		}

		public Object getItem(int position) 
		{
			return mResults.get(position);
		}

		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_recommend, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.recommend_item_title);
				holder.title_line = (ImageView) convertView.findViewById(R.id.recommend_item_title_line);
				holder.icon = (ImageView) convertView.findViewById(R.id.recommend_item_icon);
				holder.name = (TextView) convertView.findViewById(R.id.recommend_item_name);
				holder.description = (TextView) convertView.findViewById(R.id.recommend_item_description);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			RecommendResult result = mResults.get(position);
			if (mIsOfficial) 
			{
				holder.description.setVisibility(View.GONE);
				if (result.isTitle()) 
				{
					holder.title.setVisibility(View.VISIBLE);
					holder.title_line.setVisibility(View.VISIBLE);
					holder.title.setText(result.getTitleName());
				} 
				else 
				{
					holder.title.setVisibility(View.GONE);
					holder.title_line.setVisibility(View.GONE);
				}
			} 
			else 
			{
				holder.description.setVisibility(View.VISIBLE);
				holder.title.setVisibility(View.GONE);
				holder.title_line.setVisibility(View.GONE);
				holder.description.setText(result.getDescription());
			}
			holder.icon.setImageBitmap(mApplication.getRecommend(result.getIcon()));
			holder.name.setText(result.getName());
			return convertView;
		}

		class ViewHolder 
		{
			TextView  title;
			ImageView title_line;
			ImageView icon;
			TextView  name;
			TextView  description;
		}
	}
}
