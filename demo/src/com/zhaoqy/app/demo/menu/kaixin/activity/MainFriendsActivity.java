/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainFriendsActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 好友
 * @author: zhaoqy
 * @date: 2015-11-11 下午9:19:52
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.FriendsResult;
import com.zhaoqy.app.demo.menu.kaixin.item.PublicPageResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.LetterListView;
import com.zhaoqy.app.demo.menu.kaixin.view.LetterListView.OnTouchingLetterChangedListener;

public class MainFriendsActivity extends MenuKaixinBaseActivity implements OnClickListener, OnTouchingLetterChangedListener
{
	private List<FriendsResult>    mMyFriendsResults = new ArrayList<FriendsResult>(); // 当前显示的好友数据
	private List<Integer>          mMyFriendsPosition = new ArrayList<Integer>(); // 当前显示的好友的姓名的首字母的在列表中的位置
	private List<String>           mMyFriendsFirstName = new ArrayList<String>(); // 当前显示的好友的姓名的首字母数据
	private List<PublicPageResult> mMyPublicPageResults = new ArrayList<PublicPageResult>(); // 当前显示的公共主页数据
	private LetterListView         mLetter;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private EditText  mSearch;
	private TextView  mBirthday;
	private ListView  mDisplay;
	private Button    mAll;
	private Button    mPage;
	private TextUtil  mTextUtil;
	private Adapter   mAdapter;
	private boolean   mIsAll = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_friends);
		mContext = this;
		
		initView();
		setListener();
		setTextChangedListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSearch = (EditText) findViewById(R.id.friends_search);
		mBirthday = (TextView) findViewById(R.id.friends_birthday);
		mLetter = (LetterListView) findViewById(R.id.friends_letter);
		mDisplay = (ListView) findViewById(R.id.friends_display);
		mAll = (Button) findViewById(R.id.friends_all);
		mPage = (Button)findViewById(R.id.friends_page);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mAll.setOnClickListener(this);
		mPage.setOnClickListener(this);
		mLetter.setOnTouchingLetterChangedListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("好友");
		mTextUtil = new TextUtil(mApplication);
		getMyFriends();
		getPublicPage();
		mAdapter = new Adapter();
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
		case R.id.id_title_right_img:
		{
			break;
		}
		case R.id.friends_all:
		{
			//如果显示的不是好友内容则显示好友内容数据
			if (!mIsAll) 
			{
				mIsAll = true;
				mAll.setBackgroundResource(R.drawable.menu_kaixin_bottom_left_selected);
				mPage.setBackgroundResource(R.drawable.menu_kaixin_bottom_right);
				int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, mContext.getResources().getDisplayMetrics());
				mPage.setPadding(left, 0, 0, 0);
				mSearch.setText("");
				mBirthday.setVisibility(View.VISIBLE);
				mLetter.setVisibility(View.VISIBLE);
				mAdapter = new Adapter();
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		case R.id.friends_page:
		{
			//如果显示的是好友内容则显示公共主页内容数据
			if (mIsAll) 
			{
				mIsAll = false;
				mAll.setBackgroundResource(R.drawable.menu_kaixin_bottom_left);
				mPage.setBackgroundResource(R.drawable.menu_kaixin_bottom_right_selected);
				int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, mContext.getResources().getDisplayMetrics());
				mPage.setPadding(left, 0, 0, 0);
				mSearch.setText("");
				mBirthday.setVisibility(View.GONE);
				mLetter.setVisibility(View.GONE);
				mAdapter = new Adapter();
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onTouchingLetterChanged(String s) 
	{
		//根据触摸的字母,跳转到响应位置
		if (mApplication.mMyFriendsFirstNamePosition.get(s) != null) 
		{
			mDisplay.setSelection(mApplication.mMyFriendsFirstNamePosition.get(s));
		}
	}
	
	private void setTextChangedListener() 
	{
		mSearch.addTextChangedListener(new TextWatcher() 
		{
			//当文本改变时调用
			@SuppressLint("DefaultLocale")
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				//获取当前输入的内容并大写
				String searchChar = s.toString().toUpperCase();
				//如果显示的是好友内容
				if (mIsAll) 
				{
					//清除当前所有的数据
					mMyFriendsResults.clear();
					mMyFriendsPosition.clear();
					mMyFriendsFirstName.clear();
					//判断输入内容的长度
					if (searchChar.length() > 0) 
					{
						//判断是否是字母
						if (searchChar.matches("^[a-z,A-Z].*$")) 
						{
							//判断当前好友里是有存在这个字母,有的话则取出数据更新界面,否则直接更新界面
							if (mApplication.mMyFriendsGroupByFirstName.containsKey(searchChar)) 
							{
								List<FriendsResult> results = mApplication.mMyFriendsGroupByFirstName.get(searchChar);
								mMyFriendsResults.addAll(results);
								mMyFriendsFirstName.add(searchChar);
								mMyFriendsPosition.add(0);
								mAdapter.notifyDataSetChanged();
							} 
							else 
							{
								mAdapter.notifyDataSetChanged();
							}
						} 
						else 
						{
							mAdapter.notifyDataSetChanged();
						}
					} 
					else 
					{
						//输入框没内容时,获取全部好友并更新界面
						mMyFriendsResults.addAll(mApplication.mMyFriendsResults);
						mMyFriendsPosition.addAll(mApplication.mMyFriendsPosition);
						mMyFriendsFirstName.addAll(mApplication.mMyFriendsFirstName);
						mAdapter.notifyDataSetChanged();
					}
				}
				else 
				{
					//清除当前所有的数据
					mMyPublicPageResults.clear();
					//判断输入内容的长度
					if (searchChar.length() > 0) 
					{
						//判断是否是字母
						if (searchChar.matches("^[a-z,A-Z].*$")) 
						{
							//判断当前公共主页里是有存在这个字母,有的话则取出数据更新界面,否则直接更新界面
							if (mApplication.mMyPublicPageGroupByFirstName.containsKey(searchChar)) 
							{
								mMyPublicPageResults.addAll(mApplication.mMyPublicPageGroupByFirstName.get(searchChar));
								mAdapter.notifyDataSetChanged();
							}
							else 
							{
								mAdapter.notifyDataSetChanged();
							}
						} 
						else 
						{
							mAdapter.notifyDataSetChanged();
						}
					} 
					else 
					{
						//输入框没内容时,获取全部公共主页并更新界面
						mMyPublicPageResults.addAll(mApplication.mMyPublicPageResults);
						mAdapter.notifyDataSetChanged();
					}
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			public void afterTextChanged(Editable s) 
			{
			}
		});
	}

	/**
	 * 获取我的好友
	 */
	private void getMyFriends() 
	{
		if (mApplication.mMyFriendsResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_friends.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				FriendsResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new FriendsResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setName_pinyin(mTextUtil.getStringPinYin(result.getName()));
					if (!TextUtils.isEmpty(result.getName_pinyin())) 
					{
						result.setName_first(result.getName_pinyin().substring(0, 1).toUpperCase());
					}
					mApplication.mMyFriendsResults.add(result);
					if (result.getName_first().matches("^[a-z,A-Z].*$")) 
					{
						if (mApplication.mMyFriendsFirstName.contains(result.getName_first())) 
						{
							mApplication.mMyFriendsGroupByFirstName.get(result.getName_first()).add(result);
						} 
						else 
						{
							mApplication.mMyFriendsFirstName.add(result.getName_first());
							List<FriendsResult> list = new ArrayList<FriendsResult>();
							list.add(result);
							mApplication.mMyFriendsGroupByFirstName.put(result.getName_first(), list);
						}
					}
					else 
					{
						if (mApplication.mMyFriendsFirstName.contains("#")) 
						{
							mApplication.mMyFriendsGroupByFirstName.get("#").add(result);
						} 
						else 
						{
							mApplication.mMyFriendsFirstName.add("#");
							List<FriendsResult> list = new ArrayList<FriendsResult>();
							list.add(result);
							mApplication.mMyFriendsGroupByFirstName.put("#",list);
						}
					}
				}
				Collections.sort(mApplication.mMyFriendsFirstName);
				int position = 0;
				for (int i = 0; i < mApplication.mMyFriendsFirstName.size(); i++) 
				{
					mApplication.mMyFriendsFirstNamePosition.put(mApplication.mMyFriendsFirstName.get(i),position);
					mApplication.mMyFriendsPosition.add(position);
					position += mApplication.mMyFriendsGroupByFirstName.get(mApplication.mMyFriendsFirstName.get(i)).size();
				}
			} 
			catch (Exception e) 
			{
			}
		}
		mMyFriendsResults.addAll(mApplication.mMyFriendsResults);
		mMyFriendsPosition.addAll(mApplication.mMyFriendsPosition);
		mMyFriendsFirstName.addAll(mApplication.mMyFriendsFirstName);
	}

	/**
	 * 获取公共主页
	 */
	private void getPublicPage() 
	{
		if (mApplication.mMyPublicPageResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_publicpage.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				PublicPageResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new PublicPageResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setName_pinyin(mTextUtil.getStringPinYin(result.getName()));
					if (!TextUtils.isEmpty(result.getName_pinyin())) 
					{
						result.setName_first(result.getName_pinyin().substring(0, 1).toUpperCase());
					}
					mApplication.mMyPublicPageResults.add(result);
					if (result.getName_first().matches("^[a-z,A-Z].*$")) 
					{
						if (mApplication.mMyPublicPageGroupByFirstName.containsKey(result.getName_first())) 
						{
							mApplication.mMyPublicPageGroupByFirstName.get(result.getName_first()).add(result);
						} 
						else 
						{
							List<PublicPageResult> list = new ArrayList<PublicPageResult>();
							list.add(result);
							mApplication.mMyPublicPageGroupByFirstName.put(result.getName_first(), list);
						}
					} 
					else 
					{
						if (mApplication.mMyPublicPageGroupByFirstName.containsKey("#")) 
						{
							mApplication.mMyPublicPageGroupByFirstName.get("#").add(result);
						} 
						else 
						{
							List<PublicPageResult> list = new ArrayList<PublicPageResult>();
							list.add(result);
							mApplication.mMyPublicPageGroupByFirstName.put("#", list);
						}
					}
				}
				mMyPublicPageResults.addAll(mApplication.mMyPublicPageResults);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private class Adapter extends BaseAdapter implements SectionIndexer 
	{
		public int getCount() 
		{
			if (mIsAll) 
			{
				return mMyFriendsResults.size();
			} 
			else 
			{
				return mMyPublicPageResults.size();
			}

		}

		public Object getItem(int position) 
		{
			return null;
		}

		public long getItemId(int position) 
		{
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_friends, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.friends_item_alpha);
				holder.alpha_line = (ImageView) convertView.findViewById(R.id.friends_item_alpha_line);
				holder.avatar = (ImageView) convertView.findViewById(R.id.friends_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.friends_item_name);
				holder.arrow = (ImageView) convertView.findViewById(R.id.friends_item_arrow);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (mIsAll) 
			{
				int section = getSectionForPosition(position);
				final FriendsResult result = mApplication.mMyFriendsGroupByFirstName.get(mMyFriendsFirstName.get(section)).get(position - getPositionForSection(section));
				if (getPositionForSection(section) == position) 
				{
					holder.alpha.setVisibility(View.VISIBLE);
					holder.alpha_line.setVisibility(View.VISIBLE);
					holder.alpha.setText(mMyFriendsFirstName.get(section));
				} 
				else 
				{
					holder.alpha.setVisibility(View.GONE);
					holder.alpha_line.setVisibility(View.GONE);
				}
				holder.name.setText(result.getName());
				holder.avatar.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
				holder.arrow.setVisibility(View.GONE);
			} 
			else 
			{
				PublicPageResult result = mMyPublicPageResults.get(position);
				holder.alpha.setVisibility(View.GONE);
				holder.alpha_line.setVisibility(View.GONE);
				holder.arrow.setVisibility(View.VISIBLE);
				holder.avatar.setImageBitmap(mApplication.getPublicPageAvatar(result.getAvatar()));
				holder.name.setText(result.getName());
			}
			return convertView;
		}

		class ViewHolder 
		{
			TextView  alpha;
			ImageView alpha_line;
			ImageView avatar;
			TextView  name;
			ImageView arrow;
		}

		public Object[] getSections() 
		{
			return mMyFriendsFirstName.toArray();
		}

		public int getPositionForSection(int section) 
		{
			if (section < 0 || section >= mMyFriendsFirstName.size()) 
			{
				return -1;
			}
			return mMyFriendsPosition.get(section);
		}

		public int getSectionForPosition(int position) 
		{
			if (position < 0 || position >= mMyFriendsResults.size()) 
			{
				return -1;
			}
			int index = Arrays.binarySearch(mMyFriendsPosition.toArray(), position);
			return index >= 0 ? index : -index - 2;
		}
	}
}
