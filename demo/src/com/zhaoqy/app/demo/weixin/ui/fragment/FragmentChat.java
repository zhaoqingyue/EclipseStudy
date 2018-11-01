package com.zhaoqy.app.demo.weixin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.weixin.util.NetUtil;

public class FragmentChat extends Fragment implements OnClickListener
{
	public View mNetError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_weixin_chat, container, false);
		mNetError = view.findViewById(R.id.fragment_chat_neterror);
		setListener();
		return view;
	}

	private void setListener() 
	{
		mNetError.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.fragment_chat_neterror:
		{
			NetUtil.openSetNetWork(getActivity());
			break;
		}
		default:
			break;
		}
	}
}
