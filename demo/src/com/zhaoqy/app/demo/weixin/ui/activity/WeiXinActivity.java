package com.zhaoqy.app.demo.weixin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.qrcode.activity.QRCodeScanActivity;
import com.zhaoqy.app.demo.weixin.item.ActionItem;
import com.zhaoqy.app.demo.weixin.ui.fragment.FragmentChat;
import com.zhaoqy.app.demo.weixin.ui.fragment.FragmentContact;
import com.zhaoqy.app.demo.weixin.ui.fragment.FragmentFind;
import com.zhaoqy.app.demo.weixin.ui.fragment.FragmentMe;
import com.zhaoqy.app.demo.weixin.ui.view.PopupView;
import com.zhaoqy.app.demo.weixin.ui.view.PopupView.OnItemOnClickListener;

public class WeiXinActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener, OnItemOnClickListener
{
	private Context         mContext;
	private TextView        mTitle;
	private ImageView       mLeft;
	private ImageView       mRight;
	private ViewPager       mPager;
	private FragmentChat    mFragmentChat;
	private FragmentContact mFragmentContact;
	private FragmentFind    mFragmentFind;
	private FragmentMe      mFragmentMe;
	private ImageView[]     mImagebuttons = new ImageView[4];
	private TextView[]      mTextviews = new TextView[4];
	private PopupView       mPopupView;
	private int             mIndex;
	private int             mCurTabIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.activity_weixin);
		super.onCreate(savedInstanceState);
		mContext = this;
		
		initViews();
		setOnListener();
		initFragment();
		initPopWindow();
		initData();
	}

	private void initViews() 
	{
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mLeft = (ImageView) findViewById(R.id.id_title_left_img);
		mRight = (ImageView) findViewById(R.id.id_title_right_img);
		mPager = (ViewPager)findViewById(R.id.id_weixin_viewpager);
		mImagebuttons[0] = (ImageView) findViewById(R.id.id_view_chat_icon);
		mImagebuttons[1] = (ImageView) findViewById(R.id.id_view_contact_icon);
		mImagebuttons[2] = (ImageView) findViewById(R.id.id_view_find_icon);
		mImagebuttons[3] = (ImageView) findViewById(R.id.id_view_me_icon);
		mTextviews[0] = (TextView) findViewById(R.id.id_view_chat_name);
		mTextviews[1] = (TextView) findViewById(R.id.id_view_contact_name);
		mTextviews[2] = (TextView) findViewById(R.id.id_view_find_name);
		mTextviews[3] = (TextView) findViewById(R.id.id_view_me_name);
	}
	
	private void setOnListener() 
	{
		mLeft.setOnClickListener(this);
		mRight.setOnClickListener(this);
	}
	
	private void initFragment()
	{
		mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mPager.setOffscreenPageLimit(3);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(this);
	}
	
	private void initPopWindow() 
	{
		mPopupView = new PopupView(mContext, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupView.setItemOnClickListener(this);
		mPopupView.addAction(new ActionItem(this, R.string.weixin_menu_groupchat, R.drawable.weixin_menu_group));
		mPopupView.addAction(new ActionItem(this, R.string.weixin_menu_addfriend, R.drawable.weixin_menu_addfriend));
		mPopupView.addAction(new ActionItem(this, R.string.weixin_menu_qrcode, R.drawable.weixin_menu_sao));
		mPopupView.addAction(new ActionItem(this, R.string.weixin_menu_money, R.drawable.weixin_menu_pay));
	}
	
	private void initData() 
	{
		mIndex = 0;
		mTitle.setText(mContext.getResources().getString(R.string.weixin_chat));
		mLeft.setVisibility(View.VISIBLE);
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.weixin_chat_add);		
		mImagebuttons[mIndex].setSelected(true);
		mTextviews[mIndex].setTextColor(0xFF45C01A);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter
	{
	    public MyPagerAdapter(FragmentManager fm) 
	    { 
	        super(fm);  
	    }  
		
		@Override
		public Fragment getItem(int position) 
		{
			switch (position)
			{
			case 0:
			{
				mFragmentChat = new FragmentChat();
				return mFragmentChat;
			}
			case 1:
			{
				mFragmentContact = new FragmentContact();
				return mFragmentContact;
			}
			case 2:
			{
				mFragmentFind = new FragmentFind();
				return mFragmentFind;
			}
			case 3:
			{
				mFragmentMe = new FragmentMe();
				return mFragmentMe;
			}
			default:
				return null;
			}
		}  
	  
	    @Override  
	    public int getCount() 
	    {
	    	return 4; 
	    }
	}
	
	public void onTabClicked(View view) 
	{
		mRight.setVisibility(View.GONE);
		switch (view.getId()) 
		{
		case R.id.id_view_chat:
		{
			mIndex = 0;
			mTitle.setText(R.string.weixin_chat);
			mRight.setVisibility(View.VISIBLE);
			mRight.setImageResource(R.drawable.weixin_chat_add);
			break;
		}
		case R.id.id_view_contact:
		{
			mIndex = 1;
			mTitle.setText(R.string.weixin_contacts);
			mRight.setVisibility(View.GONE);
			break;
		}
		case R.id.id_view_find:
		{
			mIndex = 2;
			mTitle.setText(R.string.weixin_discover);
			mRight.setVisibility(View.GONE);
			break;
		}
		case R.id.id_view_me:
		{
			mIndex = 3;
			mTitle.setText(R.string.weixin_me);
			mRight.setVisibility(View.GONE);
			break;
		}
		default:
			break;
		}
		
		mPager.setCurrentItem(mIndex);
		mImagebuttons[mCurTabIndex].setSelected(false);
		mTextviews[mCurTabIndex].setTextColor(0xFF999999);
		mImagebuttons[mIndex].setSelected(true);
		mTextviews[mIndex].setTextColor(0xFF45C01A);
		mCurTabIndex = mIndex;
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int arg0) 
	{
		switch (arg0) 
		{
		case 0:
		{
			mIndex = 0;
			mTitle.setText(R.string.weixin_chat);
			mRight.setVisibility(View.VISIBLE);
			mRight.setImageResource(R.drawable.weixin_chat_add);
			break;
		}
		case 1:
		{
			mIndex = 1;
			mTitle.setText(R.string.weixin_contacts);
			mRight.setVisibility(View.GONE);
			break;
		}
		case 2:
		{
			mIndex = 2;
			mTitle.setText(R.string.weixin_discover);
			mRight.setVisibility(View.GONE);
			break;
		}
		case 3:
		{
			mIndex = 3;
			mTitle.setText(R.string.weixin_me);
			mRight.setVisibility(View.GONE);
			break;
		}
		default:
			break;
		}
		
		mPager.setCurrentItem(mIndex);
		mImagebuttons[mCurTabIndex].setSelected(false);
		mTextviews[mCurTabIndex].setTextColor(0xFF999999);
		mImagebuttons[mIndex].setSelected(true);
		mTextviews[mIndex].setTextColor(0xFF45C01A);
		mCurTabIndex = mIndex;
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
			mPopupView.show(findViewById(R.id.id_weixin_bar));
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(ActionItem item, int position) 
	{
		switch (position) 
		{
		case 0:
		{
			//发起聊天
			Intent intent = new Intent(mContext, ChatActivity.class);
			startActivity(intent);
			break;
		}
		case 1:
		{
			//添加朋友
			break;
		}	
		case 2:
		{
			//扫一扫
			Intent intent = new Intent(mContext, QRCodeScanActivity.class);
			startActivity(intent);
			break;
		}
		case 3:
		{
			//微信支付
			Intent intent = new Intent(mContext, PayActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
