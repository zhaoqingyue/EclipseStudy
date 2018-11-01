package com.zhaoqy.app.demo.weixin.ui.fragment;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.weixin.ui.activity.MyCodeActivity;
import com.zhaoqy.app.demo.weixin.ui.activity.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FragmentMe extends Fragment implements OnClickListener 
{
	private View     mUser;
	private TextView mPhoto;   //相册
	private TextView mCollect; //收藏
	private TextView mMoney;   //钱包
	private TextView mCard;    //卡包
	private TextView mSmail;   //表情
	private TextView mSetting; //设置
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_weixin_me, container, false);
		mUser = view.findViewById(R.id.weixin_me_user);
		mPhoto = (TextView) view.findViewById(R.id.weixin_me_photo);
		mCollect = (TextView) view.findViewById(R.id.weixin_me_collect);
		mMoney = (TextView) view.findViewById(R.id.weixin_me_money);
		mCard = (TextView) view.findViewById(R.id.weixin_me_card);
		mSmail = (TextView) view.findViewById(R.id.weixin_me_smail);
		mSetting = (TextView) view.findViewById(R.id.weixin_me_setting);
		setListener();
		return view;
	}

	private void setListener() 
	{
		mUser.setOnClickListener(this);
		mPhoto.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		mMoney.setOnClickListener(this);
		mCard.setOnClickListener(this);
		mSmail.setOnClickListener(this);
		mSetting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.weixin_me_user:
		{
			Intent intent = new Intent(getActivity(), MyCodeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.weixin_me_photo:
		{
			break;
		}
		case R.id.weixin_me_collect:
		{
			break;
		}
		case R.id.weixin_me_money:
		{
			break;
		}
		case R.id.weixin_me_card:
		{
			break;
		}
		case R.id.weixin_me_smail:
		{
			break;
		}
		case R.id.weixin_me_setting:
		{
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
