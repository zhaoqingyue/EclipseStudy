/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinSelectFriendsActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 选择好友
 * @author: zhaoqy
 * @date: 2015-11-10 上午9:43:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.FriendsResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.LetterListView;
import com.zhaoqy.app.demo.menu.kaixin.view.LetterListView.OnTouchingLetterChangedListener;

public class MenuKaixinSelectFriendsActivity extends MenuKaixinBaseActivity 
{
	private Context        mContext;
	private TextView       mCancel;
	private TextView       mTitle;
	private TextView       mSubmit;
	private EditText       mSearch;
	private ListView       mDisplay;
	private LetterListView mLetter;
	private LinearLayout   mFriendsList;
	private TextUtil       mTextUtil;
	private FriendsAdapter mAdapter;
	private List<FriendsResult> mMyFriendsResults = new ArrayList<FriendsResult>();  //当前显示的好友数据
	private List<Integer>       mMyFriendsPosition = new ArrayList<Integer>(); //当前显示的好友的姓名的首字母的在列表中的位置
	private List<String>        mMyFriendsFirstName = new ArrayList<String>(); //当前显示的好友的姓名的首字母数据
	private Map<String, Map<String, String>> mGiftFriendsList = new HashMap<String, Map<String, String>>(); //选择的好友的列表

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_select_friends);
		mContext = this;
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSubmit = (TextView) findViewById(R.id.id_title_right_text);
		mSearch = (EditText) findViewById(R.id.selectfriends_search);
		mDisplay = (ListView) findViewById(R.id.selectfriends_display);
		mLetter = (LetterListView) findViewById(R.id.selectfriends_letter);
		mFriendsList = (LinearLayout) findViewById(R.id.selectfriends_list);
		
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("选好友");
		mSubmit.setVisibility(View.VISIBLE);
		mSubmit.setText("确定");
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面并返回数据
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//赋值当前选择的好友列表,返回数据并关闭当前界面
				mApplication.mGiftFriendsList = mGiftFriendsList;
				setResult(RESULT_OK);
				finish();
			}
		});
		mSearch.addTextChangedListener(new TextWatcher() 
		{
			//当文本改变时调用
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				//清除当前所有的数据
				mMyFriendsResults.clear();
				mMyFriendsPosition.clear();
				mMyFriendsFirstName.clear();
				//获取当前输入的内容并大写
				String searchChar = s.toString().toUpperCase();
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

			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			public void afterTextChanged(Editable s) 
			{
			}
		});
		mDisplay.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//获取当前点击的好友数据
				int section = mAdapter.getSectionForPosition(position);
				final FriendsResult result = mApplication.mMyFriendsGroupByFirstName.get(mMyFriendsFirstName.get(section)).get(position - mAdapter.getPositionForSection(section));
				//判断是否是已经存在,存在则删除,否则则添加
				if (mGiftFriendsList.containsKey(result.getUid())) 
				{
					mGiftFriendsList.remove(result.getUid());
				} 
				else 
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("uid", result.getUid());
					map.put("name", result.getName());
					map.put("avatar", String.valueOf(result.getAvatar()));
					mGiftFriendsList.put(result.getUid(), map);
				}
				//更新界面
				mAdapter.notifyDataSetChanged();
				//更新头像列表
				initFriendsList();
			}
		});
		mLetter.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() 
		{
			public void onTouchingLetterChanged(String s) 
			{
				//根据触摸的字母,跳转到响应位置
				if (mApplication.mMyFriendsFirstNamePosition.get(s) != null) 
				{
					mDisplay.setSelection(mApplication.mMyFriendsFirstNamePosition.get(s));
				}
			}
		});
	}

	private void init() 
	{
		//获取当前已经选择的好友
		mGiftFriendsList.putAll(mApplication.mGiftFriendsList);
		//实例化工具类
		mTextUtil = new TextUtil(mApplication);
		//初始化头像列表
		initFriendsList();
		//获取我的好友数据
		getMyFriends();
		//添加适配器
		mAdapter = new FriendsAdapter();
		mDisplay.setAdapter(mAdapter);
	}

	/**
	 * 修改头像框显示内容
	 */
	private void initFriendsList() 
	{
		mFriendsList.removeAllViews();
		for (Map.Entry<String, Map<String, String>> entry : mGiftFriendsList.entrySet()) 
		{
			ImageView imageView = getImageView();
			imageView.setImageBitmap(mApplication.getAvatar(Integer.parseInt(entry.getValue().get("avatar"))));
			mFriendsList.addView(imageView);
			mFriendsList.invalidate();
		}
		ImageView imageView = getImageView();
		imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_kaixin_gifts_head_location));
		mFriendsList.addView(imageView);
		mFriendsList.invalidate();
	}

	/**
	 * 获取头像框控件
	 */
	private ImageView getImageView() 
	{
		ImageView imageView = new ImageView(mContext);
		int widthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthAndHeight, widthAndHeight);
		imageView.setLayoutParams(params);
		imageView.setPadding(4, 4, 4, 4);
		return imageView;
	}

	/**
	 * 获取我的好友数据
	 */
	@SuppressLint("DefaultLocale")
	private void getMyFriends() 
	{
		if (mApplication.mMyFriendsResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = getAssets().open("data/my_friends.KX");
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
							mApplication.mMyFriendsGroupByFirstName.put("#", list);
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
		//添加数据到当前显示的数据
		mMyFriendsResults.addAll(mApplication.mMyFriendsResults);
		mMyFriendsPosition.addAll(mApplication.mMyFriendsPosition);
		mMyFriendsFirstName.addAll(mApplication.mMyFriendsFirstName);
	}

	private class FriendsAdapter extends BaseAdapter implements SectionIndexer 
	{
		public int getCount() 
		{
			return mMyFriendsResults.size();
		}

		public Object getItem(int position) 
		{
			return mMyFriendsResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_select_friends, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.selectfriends_item_alpha);
				holder.alpha_line = (ImageView) convertView.findViewById(R.id.selectfriends_item_alpha_line);
				holder.avatar = (ImageView) convertView.findViewById(R.id.selectfriends_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.selectfriends_item_name);
				holder.status = (ImageView) convertView.findViewById(R.id.selectfriends_item_status);
				holder.style = (CheckBox) convertView.findViewById(R.id.selectfriends_item_style);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
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
			holder.status.setVisibility(View.GONE);
			if (mGiftFriendsList.containsKey(result.getUid())) 
			{
				holder.style.setChecked(true);
			} 
			else 
			{
				holder.style.setChecked(false);
			}
			holder.style.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					if (mGiftFriendsList.containsKey(result.getUid())) 
					{
						mGiftFriendsList.remove(result.getUid());
					}
					else 
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("uid", result.getUid());
						map.put("name", result.getName());
						map.put("avatar", String.valueOf(result.getAvatar()));
						mGiftFriendsList.put(result.getUid(), map);
					}
					notifyDataSetChanged();
					initFriendsList();
				}
			});
			return convertView;
		}

		class ViewHolder 
		{
			TextView  alpha;
			ImageView alpha_line;
			ImageView avatar;
			TextView  name;
			ImageView status;
			CheckBox  style;
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

	public void onBackPressed() 
	{
		setResult(RESULT_CANCELED);
		finish();
	}
}
