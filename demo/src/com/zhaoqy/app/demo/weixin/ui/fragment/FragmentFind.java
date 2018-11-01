package com.zhaoqy.app.demo.weixin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.qrcode.activity.QRCodeScanActivity;
import com.zhaoqy.app.demo.weixin.ui.activity.FriendCircleActivity;

public class FragmentFind extends Fragment implements OnClickListener 
{
	private TextView mComent; //朋友圈
	private TextView mScan;   //扫一扫
	private TextView mShake;  //摇一摇
	private TextView mNearby; //附近的人
	private TextView mDrift;  //漂流瓶
	private TextView mShop;   //购物
	private TextView mGame;   //游戏
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_weixin_find, container, false);
		mComent = (TextView) view.findViewById(R.id.weixin_find_moments);
		mScan = (TextView) view.findViewById(R.id.weixin_find_scan);
		mShake = (TextView) view.findViewById(R.id.weixin_find_shake);
		mNearby = (TextView) view.findViewById(R.id.weixin_find_nearby);
		mDrift = (TextView) view.findViewById(R.id.weixin_find_drift);
		mShop = (TextView) view.findViewById(R.id.weixin_find_shop);
		mGame = (TextView) view.findViewById(R.id.weixin_find_game);
		setListener();
		return view;
	}

	private void setListener() 
	{
		mComent.setOnClickListener(this);
		mScan.setOnClickListener(this);
		mShake.setOnClickListener(this);
		mNearby.setOnClickListener(this);
		mDrift.setOnClickListener(this);
		mShop.setOnClickListener(this);
		mGame.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.weixin_find_moments:
		{
			Intent intent = new Intent(getActivity(), FriendCircleActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.weixin_find_scan:
		{
			Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.weixin_find_shake:
		{
			break;
		}
		case R.id.weixin_find_nearby:
		{
			break;
		}
		case R.id.weixin_find_drift:
		{
			break;
		}
		case R.id.weixin_find_shop:
		{
			break;
		}
		case R.id.weixin_find_game:
		{
			break;
		}
		default:
			break;
		}
	}
}
