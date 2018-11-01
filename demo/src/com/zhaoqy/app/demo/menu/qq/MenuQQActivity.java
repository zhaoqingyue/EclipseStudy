package com.zhaoqy.app.demo.menu.qq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class MenuQQActivity extends FragmentActivity implements OnClickListener  
{
	private SlideMenu     resideMenu;
	private SlideMenuItem itemHuiyuan;
	private SlideMenuItem itemQianbao;
	private SlideMenuItem itemZhuangban;
	private SlideMenuItem itemShoucang;
	private SlideMenuItem itemXiangce;
	private SlideMenuItem itemFile;
	private SlideMenuInfo info;
	private TextView      mNews;
	private TextView      mContacts;
	private TextView      mDongtai;
	private Button        mLeftMenu;
	private boolean       mClosed = true;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_qq);

		setUpMenu();
		changeFragment(new NewsFragment());
		setListener();
	}

	@SuppressWarnings("deprecation")
	private void setUpMenu() 
	{
		mLeftMenu = (Button) findViewById(R.id.id_menu_qq_title);
		mNews = (TextView) findViewById(R.id.id_menu_qq_news);
		mContacts = (TextView) findViewById(R.id.id_menu_qq_contacts);
		mDongtai = (TextView) findViewById(R.id.id_menu_qq_dongtai);

		resideMenu = new SlideMenu(this);
		resideMenu.setBackground(R.color.blue1);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);
		// 禁止使用右侧菜单
		resideMenu.setDirectionDisable(SlideMenu.DIRECTION_RIGHT);

		// create menu items;
		itemHuiyuan = new SlideMenuItem(this, R.drawable.ic_launcher, "开通会员");
		itemQianbao = new SlideMenuItem(this, R.drawable.ic_launcher, "QQ钱包");
		itemZhuangban = new SlideMenuItem(this, R.drawable.ic_launcher, "个性装扮");
		itemShoucang = new SlideMenuItem(this, R.drawable.ic_launcher, "我的收藏");
		itemXiangce = new SlideMenuItem(this, R.drawable.ic_launcher, "我的相册");
		itemFile = new SlideMenuItem(this, R.drawable.ic_launcher, "我的文件");

		resideMenu.addMenuItem(itemHuiyuan, SlideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemQianbao, SlideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemZhuangban, SlideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemShoucang, SlideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemXiangce, SlideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemFile, SlideMenu.DIRECTION_LEFT);

		info = new SlideMenuInfo(this, R.drawable.menu_qq_icon_profile, "开心小萝卜头", "32 级");
	}

	private void setListener() 
	{
		resideMenu.addMenuInfo(info);
		itemHuiyuan.setOnClickListener(this);
		itemQianbao.setOnClickListener(this);
		itemZhuangban.setOnClickListener(this);
		itemShoucang.setOnClickListener(this);
		itemXiangce.setOnClickListener(this);
		itemFile.setOnClickListener(this);
		mNews.setOnClickListener(this);
		mContacts.setOnClickListener(this);
		mDongtai.setOnClickListener(this);
		info.setOnClickListener(this);

		mLeftMenu.setOnClickListener(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) 
	{
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.id_menu_qq_title:
		{
			resideMenu.openMenu(SlideMenu.DIRECTION_LEFT);
			break;
		}
		case R.id.id_menu_qq_news:
		{
			changeFragment(new NewsFragment());
			break;
		}
		case R.id.id_menu_qq_contacts:
		{
			changeFragment(new ContactsFragment());
			break;
		}	
		case R.id.id_menu_qq_dongtai:
		{
			changeFragment(new DongtaiFragment());
			break;
		}	
		default:
			break;
		}
		
		if (view == itemHuiyuan) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "会员");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		} 
		else if (view == itemQianbao) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "钱包");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		} 
		else if (view == itemZhuangban) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "装扮");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		} 
		else if (view == itemShoucang) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "收藏");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		} 
		else if (view == itemXiangce) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "相册");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		} 
		else if (view == itemFile) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "文件");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		} 
		else if (view == info) 
		{
			Intent intent = new Intent();
			intent.putExtra("text", "个人信息");
			intent.setClass(getApplicationContext(), MenuQQSettingActivity.class);
			startActivity(intent);
		}
	}

	private SlideMenu.OnMenuListener menuListener = new SlideMenu.OnMenuListener() 
	{
		@Override
		public void openMenu() 
		{
			mClosed = false;
			mLeftMenu.setVisibility(View.GONE);
		}

		@Override
		public void closeMenu() 
		{
			mClosed = true;
			mLeftMenu.setVisibility(View.VISIBLE);
		}
	};

	private void changeFragment(Fragment targetFragment) 
	{
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_menu_qq_main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	public SlideMenu getResideMenu() 
	{
		return resideMenu;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (mClosed) 
			{
				finish();
			}
			else 
			{
				resideMenu.closeMenu();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
